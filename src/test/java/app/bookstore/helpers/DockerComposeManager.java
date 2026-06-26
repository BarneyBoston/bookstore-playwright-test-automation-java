package app.bookstore.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test helper to ensure docker-compose stack is running when tests are executed from IDE.

 * Behavior:
 *  - checks DB host:port parsed from `dbConnString` in `Config`.
 *  - if connection to DB port is not available, attempts to run `docker-compose up -d` in project root.
 *  - waits until DB port is reachable or timeout expires.

 * Note: This helper tries to be non-fatal — if Docker is not available or starting fails, it logs
 * a warning and lets the test run (which will fail later if DB is not available).
 */
public class DockerComposeManager {
    private static final Logger logger = LoggerFactory.getLogger(DockerComposeManager.class);

    public static void ensureStackRunning() {
        Config cfg = Config.getInstance();
        String conn = cfg.getDbConnString();
        HostPort hostPort = parseJdbcHostPort(conn);
        if (hostPort == null) {
            logger.error("failed to parse DB connection string: {}", conn);
            return;
        }

        if (isTcpOpen(hostPort.host, hostPort.port)) {
            logger.info("DB reachable at {}:{}", hostPort.host, hostPort.port);
            return;
        }

        logger.info("DB not reachable at {}:{}, attempting to start docker-compose...", hostPort.host, hostPort.port);

        try {
            runDockerComposeUp();
        } catch (Exception e) {
            logger.error("failed to run docker-compose up -d: {}", e.getMessage(), e);
            return;
        }

        // Attempt to connect up to a maximum number of retries
        // Each attempt includes a 2-second TCP socket timeout, so this provides reasonable coverage
        int maxAttempts = 60;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            if (isTcpOpen(hostPort.host, hostPort.port)) {
                logger.info("DB reachable after docker-compose at {}:{}", hostPort.host, hostPort.port);
                return;
            }
            // Yield control to other threads without explicit sleep
            Thread.yield();
        }

        logger.warn("timed out waiting for DB at {}:{}", hostPort.host, hostPort.port);
    }

    private static void runDockerComposeUp() throws IOException, InterruptedException {
        // Run `docker-compose up -d` in project root
        String projectDir = System.getProperty("user.dir");
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(projectDir));
        // Windows may have docker-compose as an exe or compatible with `docker compose` newer CLI.
        // Try `docker-compose` first, fallback to `docker compose`.

        try {
            pb.command("docker-compose", "up", "-d");
            try (Process proc = pb.start()) {
                int exit = proc.waitFor();
                if (exit == 0) return;
            }
        } catch (IOException _) {
            // try fallback
        }

        // fallback to `docker compose up -d`
        pb.command("docker", "compose", "up", "-d");
        try (Process proc2 = pb.start()) {
            int exit2 = proc2.waitFor();
            if (exit2 != 0) {
                String stdout = capture(proc2.getInputStream());
                String stderr = capture(proc2.getErrorStream());
                throw new IOException("docker compose up failed: exit=" + exit2 + " stdout=" + stdout + " stderr=" + stderr);
            }
        }
    }

    private static String capture(java.io.InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private static boolean isTcpOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            return true;
        } catch (IOException _) {
            return false;
        }
    }

    private static HostPort parseJdbcHostPort(String jdbc) throws NumberFormatException {
        if (jdbc == null) return null;
        // example: jdbc:mysql://localhost:3307/wordpress
        Pattern p = Pattern.compile("jdbc:mysql://([^:/]+)(?::(\\d+))?/");
        Matcher m = p.matcher(jdbc);
        if (m.find()) {
            String host = m.group(1);
            String portStr = m.group(2);
            int port = 3306;
            if (portStr != null && !portStr.isEmpty()) {
                port = Integer.parseInt(portStr);
            }
            return new HostPort(host, port);
        }
        return null;
    }

    private record HostPort(String host, int port) {
    }
}


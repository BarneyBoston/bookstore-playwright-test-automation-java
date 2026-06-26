package app.bookstore.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Getter
@SuppressWarnings("unused")
public class Config {
    private static volatile Config instance;
    private String dbUsername;
    private String dbPassword;
    private String dbConnString;
    private String baseUri;
    private String consumerKey;
    private String consumerSecret;
    private String baseUrl;
    private String browser;
    private Boolean isHeadless;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = loadConfig();
                }
            }
        }
        return instance;
    }

    private static Config loadConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String env = System.getProperty("ENV");
        if (env == null || env.isEmpty()) {
            env = System.getenv().getOrDefault("ENV", "local");
        }

        String configFilePath = Paths.get("src", "test", "resources", "config-" + env + ".yaml").toString();

        try {
            return mapper.readValue(new File(configFilePath), Config.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + configFilePath, e);
        }
    }
}

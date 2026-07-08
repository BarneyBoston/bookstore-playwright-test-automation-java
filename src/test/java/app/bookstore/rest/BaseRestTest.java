package app.bookstore.rest;

import app.bookstore.api.BookStoreApiController;
import app.bookstore.db.BookStoreDB;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseRestTest {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final List<Playwright> allPlaywrightInstances = new CopyOnWriteArrayList<>();
    private static final ThreadLocal<APIRequestContext> requestThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BookStoreApiController> controllerThreadLocal = new ThreadLocal<>();
    public static final Logger log = LoggerFactory.getLogger(BaseRestTest.class);


    protected APIRequestContext request() {
        return requestThreadLocal.get();
    }

    protected BookStoreApiController controller() {
        return controllerThreadLocal.get();
    }

    @BeforeSuite
    public void beforeSuite(){
        // Ensure Docker compose stack is running locally (if available) so DB/WordPress are reachable
        try {
            app.bookstore.helpers.DockerComposeManager.ensureStackRunning();
        } catch (Exception e) {
            // Don't fail suite start here — log and continue; tests will fail later if DB is not available
            log.warn("Warning: failed to ensure docker-compose stack is running: {}", e.getMessage());
        }
    }

    @BeforeMethod
    public void setup() {
        BookStoreDB.init();

        if (playwrightThreadLocal.get() == null) {
            Playwright pw = Playwright.create();
            playwrightThreadLocal.set(pw);
            allPlaywrightInstances.add(pw);
        }

        APIRequestContext context = playwrightThreadLocal.get().request().newContext(
                new APIRequest.NewContextOptions()
                        .setExtraHTTPHeaders(buildAuthHeaders())
        );
        requestThreadLocal.set(context);
        controllerThreadLocal.set(new BookStoreApiController(context));
    }

    @AfterMethod
    public void cleanUp() {
        if (request() != null) request().dispose();
        requestThreadLocal.remove();
        controllerThreadLocal.remove();
        BookStoreDB.remove();
    }

    @AfterSuite
    public void afterSuite() {
        allPlaywrightInstances.forEach(Playwright::close);
        allPlaywrightInstances.clear();
    }

    private Map<String, String> buildAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}

package app.bookstore.rest.base;

import app.bookstore.api.BookStoreApiController;
import app.bookstore.db.BookStoreDB;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRestTest {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> requestThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BookStoreApiController> controllerThreadLocal = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(BaseRestTest.class);


    protected APIRequestContext request() {
        return requestThreadLocal.get();
    }

    protected BookStoreApiController controller() {
        return controllerThreadLocal.get();
    }

    @BeforeSuite
    public void ensureDockerRunsLocally(){
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

        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        APIRequestContext context = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setExtraHTTPHeaders(buildAuthHeaders())
        );
        requestThreadLocal.set(context);
        controllerThreadLocal.set(new BookStoreApiController(context));
    }

    @AfterMethod
    public void cleanUp() {
        if (request() != null) request().dispose();
        if (playwrightThreadLocal.get() != null) playwrightThreadLocal.get().close();
        requestThreadLocal.remove();
        playwrightThreadLocal.remove();
        controllerThreadLocal.remove();
        BookStoreDB.remove();
    }


    private Map<String, String> buildAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}

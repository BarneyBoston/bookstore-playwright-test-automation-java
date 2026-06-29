package app.bookstore.rest.base;

import app.bookstore.api.BookStoreApiController;
import app.bookstore.db.BookStoreDB;
import app.bookstore.helpers.Config;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRestTest {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> requestThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BookStoreApiController> controllerThreadLocal = new ThreadLocal<>();

    protected APIRequestContext request() {
        return requestThreadLocal.get();
    }

    protected BookStoreApiController controller() {
        return controllerThreadLocal.get();
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

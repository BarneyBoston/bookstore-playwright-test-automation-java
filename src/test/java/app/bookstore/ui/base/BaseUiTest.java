package app.bookstore.ui.base;

import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.helpers.NoSuchBrowserException;
import app.bookstore.ui.helpers.PlaywrightManager;
import app.bookstore.ui.pages.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import app.bookstore.helpers.Config;
import app.bookstore.ui.helpers.BrowserFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public abstract class BaseUiTest {
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static volatile Browser browser;
    private static final List<Playwright> allPlaywrightInstances = new CopyOnWriteArrayList<>();
    private static final ThreadLocal<Store> storeThreadLocal = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(BaseUiTest.class);

    protected Store store() {
        return storeThreadLocal.get();
    }

    @BeforeSuite
    public void beforeSuite() {
        // Ensure Docker compose stack is running locally (if available) so DB/WordPress are reachable
        try {
            app.bookstore.helpers.DockerComposeManager.ensureStackRunning();
        } catch (Exception e) {
            // Don't fail suite start here — log and continue; tests will fail later if DB is not available
            log.warn("Warning: failed to ensure docker-compose stack is running: {}", e.getMessage());
        }
    }

    @BeforeMethod
    public void setUp() {
        BookStoreDB.init();

        if (playwrightThreadLocal.get() == null) {
            Playwright pw = Playwright.create();
            playwrightThreadLocal.set(pw);
            allPlaywrightInstances.add(pw);
        }

        if (browser == null) {
            synchronized (BaseUiTest.class) {
                if (browser == null) {
                    var browserType = Config.getInstance().getBrowser();
                    try {
                        browser = BrowserFactory.getBrowser(playwrightThreadLocal.get(), browserType);
                    } catch (NoSuchBrowserException e) {
                        throw new RuntimeException("Failed to initialize browser factory", e);
                    }
                }
            }
        }

        BrowserContext browserContext = browser.newContext();
        try {
            Page targetPage = browserContext.newPage();
            PlaywrightManager.setBrowserContext(browserContext);
            PlaywrightManager.setPage(targetPage);
            storeThreadLocal.set(new Store(targetPage));
            targetPage.navigate(Config.getInstance().getBaseUrl());
            targetPage.waitForLoadState();
        } catch (Exception e) {
            browserContext.close();
            throw e;
        }
    }

    @AfterMethod
    public void tearDown(ITestResult testResult) {
        if (!testResult.isSuccess() && testResult.getStatus() != ITestResult.SKIP && PlaywrightManager.getPage() != null) {
            saveScreenshot();
        }
        PlaywrightManager.cleanUp();
        storeThreadLocal.remove();
        BookStoreDB.remove();
    }

    @AfterSuite
    public void afterSuite() {
        if (browser != null) browser.close();
        allPlaywrightInstances.forEach(Playwright::close);
        allPlaywrightInstances.clear();
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot() {
        // In Playwright saving screenshot as a byte table is native
        return PlaywrightManager.getPage().screenshot();
    }
}

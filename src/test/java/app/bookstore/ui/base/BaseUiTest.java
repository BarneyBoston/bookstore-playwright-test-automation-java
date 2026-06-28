package app.bookstore.ui.base;

import app.bookstore.db.BookStoreDB;
import app.bookstore.playwright.helpers.NoSuchBrowserException;
import app.bookstore.playwright.helpers.PlaywrightManager;
import app.bookstore.playwright.pages.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import app.bookstore.helpers.Config;
import app.bookstore.playwright.helpers.BrowserFactory;


public abstract class BaseUiTest {
    private static volatile Playwright playwright;
    private static volatile Browser browser;
    private static final ThreadLocal<Store> storeThreadLocal = new ThreadLocal<>();

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
            System.err.println("Warning: failed to ensure docker-compose stack is running: " + e.getMessage());
        }
        // playwright server is to be initiated once per whole test suite
        playwright = Playwright.create();

        var browserType = Config.getInstance().getBrowser();
        try {
            browser = BrowserFactory.getBrowser(playwright, browserType);
        } catch (NoSuchBrowserException e) {
            throw new RuntimeException("Failed to initialize browser factory", e);
        }
    }

    @BeforeMethod
    public void setUp() {
        BookStoreDB.init();

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
        if (playwright != null) playwright.close();
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot() {
        // In Playwright saving screenshot as a byte table is native
        return PlaywrightManager.getPage().screenshot();
    }
}

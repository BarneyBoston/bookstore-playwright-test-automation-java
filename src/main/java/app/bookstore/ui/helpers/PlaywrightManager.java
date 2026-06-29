package app.bookstore.ui.helpers;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public abstract class PlaywrightManager {
    private PlaywrightManager() {
        /* This utility class should not be instantiated */
    }

    private static final ThreadLocal<BrowserContext> browserContextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static void setBrowserContext(BrowserContext context) {
        browserContextThreadLocal.set(context);
    }

    public static BrowserContext getBrowserContext() {
        return browserContextThreadLocal.get();
    }

    public static void setPage(Page page) {
        pageThreadLocal.set(page);
    }

    public static Page getPage() {
        return pageThreadLocal.get();
    }

    public static void cleanUp() {
        if (getPage() != null) getPage().close();
        if (getBrowserContext() != null) getBrowserContext().close();
        pageThreadLocal.remove();
        browserContextThreadLocal.remove();
    }
}

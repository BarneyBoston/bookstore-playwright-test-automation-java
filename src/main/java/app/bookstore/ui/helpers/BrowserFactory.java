package app.bookstore.ui.helpers;

import app.bookstore.helpers.Config;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public abstract class BrowserFactory {
    private BrowserFactory() {
        /* This utility class should not be instantiated */
    }


    private static boolean isHeadless() {
        return Config.getInstance().getIsHeadless();
    }

    public static Browser getBrowser(Playwright playwright, String browserName) throws NoSuchBrowserException {

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(isHeadless());

        switch (browserName.toLowerCase()) {
            case "chrome", "chromium" -> {
                return playwright.chromium().launch(options);
            }
            case "firefox" -> {
                return playwright.firefox().launch(options);
            }
            case "edge" -> {
                // Playwright supports Edge as a channel within chromium
                options.setChannel("msedge");
                return playwright.chromium().launch(options);
            }
            default -> throw new NoSuchBrowserException(browserName);
        }
    }
}

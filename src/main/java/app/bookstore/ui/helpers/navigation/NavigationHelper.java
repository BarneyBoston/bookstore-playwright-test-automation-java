package app.bookstore.ui.helpers.navigation;

import app.bookstore.helpers.Config;
import com.microsoft.playwright.Page;

public class NavigationHelper {

    private final Page page;

    public NavigationHelper(Page page) {
        this.page = page;
    }

    public void goTo(AppPage appPage) {
        String url = Config.getInstance().getBaseUrl() + appPage.path();
        page.navigate(url);
        page.waitForLoadState();
    }

    public void goTo(AppPage appPage, Object... params) {
        String url = Config.getInstance().getBaseUrl() + appPage.path(params);
        page.navigate(url);
        page.waitForLoadState();
    }

    public void goToPath(String rawPath) {
        page.navigate(Config.getInstance().getBaseUrl() + rawPath);
        page.waitForLoadState();
    }
}
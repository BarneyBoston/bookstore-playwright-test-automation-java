package app.bookstore.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;


public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    public void waitForPageToLoad() {
        page.waitForLoadState(LoadState.LOAD);
    }

    public void waitForNetworkIdle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
}
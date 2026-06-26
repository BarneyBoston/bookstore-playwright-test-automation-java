package app.bookstore.playwright.pages;

import com.microsoft.playwright.Page;

public class Pages {

    private final Page page;
    private MainPage mainPage;

    public Pages(Page page) {
        this.page = page;
    }

    public MainPage mainPage() {
        if (mainPage == null) {
            mainPage = new MainPage(page);
        }
        return mainPage;
    }
}

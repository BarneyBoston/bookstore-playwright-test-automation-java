package app.bookstore.ui.pages;

import com.microsoft.playwright.Page;

public class Store {

    private final Page page;
    private MainPage mainPage;
    private PreviewCartPage previewCartPage;
    private NavigationBar navigationBar;

    public Store(Page page) {
        this.page = page;
    }

    public MainPage mainPage() {
        if (mainPage == null) {
            mainPage = new MainPage(page);
        }
        return mainPage;
    }

    public PreviewCartPage previewCartPage() {
        if (previewCartPage == null) {
            previewCartPage = new PreviewCartPage(page);
        }
        return previewCartPage;
    }

    public NavigationBar navigationBar(){
        if (navigationBar == null) {
            navigationBar = new NavigationBar(page);
        }
        return navigationBar;
    }
}

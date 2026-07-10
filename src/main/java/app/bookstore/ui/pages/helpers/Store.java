package app.bookstore.ui.pages.helpers;

import app.bookstore.ui.helpers.navigation.NavigationHelper;
import app.bookstore.ui.pages.CartPage;
import app.bookstore.ui.pages.MainPage;
import app.bookstore.ui.pages.PreviewCartPage;
import com.microsoft.playwright.Page;

public class Store {

    private final Page page;
    private final NavigationHelper navigationHelper;
    private MainPage mainPage;
    private PreviewCartPage previewCartPage;
    private NavigationBar navigationBar;
    private CartPage cartPage;
    private Notifications notifications;

    public Store(Page page) {
        this.page = page;
        this.navigationHelper = new NavigationHelper(page);
    }

    public NavigationHelper navigation() {
        return navigationHelper;
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

    public NavigationBar navigationBar() {
        if (navigationBar == null) {
            navigationBar = new NavigationBar(page);
        }
        return navigationBar;
    }

    public CartPage cartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(page);
        }
        return cartPage;
    }

    public Notifications notifications() {
        if (notifications == null) {
            notifications = new Notifications(page);
        }
        return notifications;
    }
}

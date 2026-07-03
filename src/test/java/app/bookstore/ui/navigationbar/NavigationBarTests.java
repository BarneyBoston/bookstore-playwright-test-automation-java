package app.bookstore.ui.navigationbar;

import app.bookstore.db.BookStoreDB;
import app.bookstore.helpers.Config;
import app.bookstore.ui.base.BaseUiTest;
import app.bookstore.ui.helpers.PlaywrightManager;
import app.bookstore.ui.helpers.UiAssertions;
import app.bookstore.ui.pages.NavigationBar;
import io.qameta.allure.Epic;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static app.bookstore.ui.helpers.UiAssertions.el;


@Epic("Navigation Bar Tests")
public class NavigationBarTests extends BaseUiTest {

    @DataProvider(name = "appPages")
    public Object[][] appPages() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();
        return new Object[][] {
                { "/" },
                { "/my-account/lost-password/" },
                { "/my-account" },
                { String.format("/product/%s", dbTitle) },
                { "/wishlist/" },
        };
    }

    @Test(dataProvider = "appPages", description = "Verify all navigation bar element are present on given pages")
    public void should_navigation_bar_be_on_every_page(String path) {
        PlaywrightManager.getPage().navigate(Config.getInstance().getBaseUrl() + path);

        NavigationBar navigationBar = store().navigationBar();

        UiAssertions.assertAllVisible(
                el("Cart button", navigationBar.getCartPageButton()),
                el("My account button", navigationBar.getMyAccountPageButton()),
                el("Wishlist button", navigationBar.getWishlistPageButton())
        );
    }
}

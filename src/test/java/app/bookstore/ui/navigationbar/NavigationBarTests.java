package app.bookstore.ui.navigationbar;

import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.UiAssertions;
import app.bookstore.ui.helpers.navigation.AppPage;
import app.bookstore.ui.pages.helpers.NavigationBar;
import io.qameta.allure.Epic;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static app.bookstore.ui.helpers.UiAssertions.el;


@Epic("Navigation Bar Tests")
public class NavigationBarTests extends BaseUiTest {

    @DataProvider(name = "appPages")
    public Object[][] appPages() {
        var dbSlug = BookStoreDB.getDb().selectRandomActiveProduct().getSlug();
        return new Object[][]{
                {AppPage.HOME, new Object[]{}},
                {AppPage.LOST_PASSWORD, new Object[]{}},
                {AppPage.MY_ACCOUNT, new Object[]{}},
                {AppPage.PRODUCT, new Object[]{dbSlug}},
                {AppPage.WISHLIST, new Object[]{}},
        };
    }

    @Test(dataProvider = "appPages")
    public void should_all_navigation_bar_elements_be_visible(AppPage appPage, Object[] params) {
        store().navigation().goTo(appPage, params);

        NavigationBar navigationBar = store().navigationBar();
        UiAssertions.assertAllVisible(
                el("Cart button", navigationBar.getCartPageButton()),
                el("My account button", navigationBar.getMyAccountPageButton()),
                el("Wishlist button", navigationBar.getWishlistPageButton())
        );
    }
}

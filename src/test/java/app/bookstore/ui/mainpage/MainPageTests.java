package app.bookstore.ui.mainpage;

import app.bookstore.base.BaseUiTest;
import app.bookstore.db.BookStoreDB;
import app.bookstore.playwright.helpers.PlaywrightManager;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MainPageTests extends BaseUiTest {

    @Test(description = "Verify searching a product redirects to the product page")
    public void should_search_product_redirect_to_product_page() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().searchForProduct(dbTitle);

        assertThat(PlaywrightManager.getPage().url())
                .describedAs("\"Searching Product should redirect to product page")
                .contains("/product");
    }
}

package app.bookstore.ui.mainpage;

import app.bookstore.db.models.PostRecord;
import app.bookstore.ui.base.BaseUiTest;
import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.helpers.PlaywrightManager;
import io.qameta.allure.Epic;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Main Page Tests")
public class MainPageTests extends BaseUiTest {

    @Test(description = "Verify user is on the main page")
    public void should_user_be_on_the_main_page() {
        assertThat(PlaywrightManager.getPage()).hasURL("http://localhost:8080/");
        assertThat(PlaywrightManager.getPage()).hasTitle("Test App – Just another WordPress site");
    }

    @Test(description = "Verify searching a product redirects to the product page")
    public void should_search_product_redirect_to_product_page() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().searchForProduct(dbTitle);

        assertThat(PlaywrightManager.getPage().url())
                .describedAs("Searching Product should redirect to product page")
                .contains("/product");
    }

    @Test(description = "Verify all product titles from UI match those from the database")
    public void should_all_product_titles_from_ui_match_db() {
        var dbTitles = BookStoreDB.getDb().selectActiveProducts()
                .stream()
                .map(PostRecord::getName)
                .toList();

        var uiTitles = store().mainPage().getProductTitles();

        assertThat(dbTitles)
                .containsExactlyInAnyOrderElementsOf(uiTitles)
                .hasSameSizeAs(uiTitles);
    }

    @Test(description = "Verify that default sorting option sorts products correctly")
    public void sort_by_default() {
        store().mainPage().selectSortingOption("Default sorting");
        var uiTitles = store().mainPage().getProductTitles();

        assertThat(uiTitles).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @DataProvider(name = "sorting_options")
    public Object[][] page_two_elements_navigation_data() {
        return new Object[][]{
                {"Sort by price: low to high", Comparator.naturalOrder()},
                {"Sort by price: high to low", Comparator.naturalOrder().reversed()},
        };
    }

    @Test(dataProvider = "sorting_options", description = "Verify sorting by price works as expected")
    public void sort_by(String sortingOption, Comparator<Double> comparator) {
        store().mainPage().selectSortingOption(sortingOption);
        var prices = store().mainPage().getPrices();

        assertThat(prices).isSortedAccordingTo(comparator);
    }

    @Test(description = "Verify adding product to cart opens the cart preview")
    public void should_add_to_cart_open_cart_preview() {
        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(bookName);

        assertThat(store().previewCartPage().getPreviewCartContext()).isVisible();
    }
}

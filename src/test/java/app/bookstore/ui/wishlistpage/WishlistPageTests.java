package app.bookstore.ui.wishlistpage;

import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.navigation.AppPage;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Wishlist Page Tests")
public class WishlistPageTests extends BaseUiTest {

    @Test(description = "Verify that empty wishlist displays all expected elements")
    public void should_empty_wishlist_have_proper_elements_test() {
        store().navigation().goTo(AppPage.WISHLIST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(store().wishlistPage().getWishlistTitle().innerText()).isEqualTo("My wishlist");
            softly.assertThat(store().wishlistPage().getEmptyWishlistText().innerText()).isEqualTo("No products added to the wishlist");
        });
    }

    @Test(description = "Verify that clicking on added product in wishlist redirects to product page")
    public void should_added_product_clicked_redirect_to_product_page_test() {
        var book = BookStoreDB.getDb().selectActiveProducts().getLast();
        var bookSlug = book.getSlug();
        var bookName = book.getName();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().addToWishList();
        store().productPage().viewWishList();
        store().wishlistPage().clickChosenProduct(bookName);

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().productPage().getDescriptionTabButton().isVisible()).as("Description tab button is not displayed and enabled").isTrue();
            softly.assertThat(store().productPage().getContentsTabButton().isVisible()).as("Contents tab button is not displayed and enabled").isTrue();
            softly.assertThat(store().productPage().getReviewsTabButton().isVisible()).as("Reviews tab button is not displayed and enabled").isTrue();
            softly.assertThat(store().productPage().getRelatedProductsSection().isVisible()).as("Related products section is not displayed").isTrue();
        });
    }

    @Test(description = "Verify that adding a product from wishlist to cart works correctly")
    public void should_add_to_cart_work_test() {
        var book = BookStoreDB.getDb().selectActiveProducts().getLast();
        var bookSlug = book.getSlug();
        var bookName = book.getName();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().addToWishList();
        store().productPage().viewWishList();
        store().wishlistPage().clickAddToCartByProduct(bookName);
        store().previewCartPage().viewMyCart();

        Assertions.assertThat(store().cartPage().getProductNames().nth(0).innerText())
                .as("Product name do not match expected value")
                .isEqualTo(bookName);
    }
}

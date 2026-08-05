package app.bookstore.ui.previewcartpage;

import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.PlaywrightManager;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Preview Cart Page Tests")
public class PreviewCartPageTests extends BaseUiTest {

    @Test(description = "Verify all elements of an empty preview cart are visible")
    public void empty_preview_cart_elements_test() {
        store().navigationBar().goToPreviewCart();

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().previewCartPage().getEmptyCartText().isEnabled()).isTrue();
            softly.assertThat(store().previewCartPage().getStartShoppingButton().isEnabled()).isTrue();
        });
    }

    @Test(description = "Start shopping button returns user to main page")
    public void should_start_shopping_return_user_to_main_page() {
        store().navigationBar().goToPreviewCart();
        store().previewCartPage().clickStartShopping();

        assertThat(PlaywrightManager.getPage()).hasURL("http://localhost:8080/");
        assertThat(PlaywrightManager.getPage()).hasTitle("Test App – Just another WordPress site");
    }

    @Test(description = "Verify all elements of a filled preview cart are visible")
    public void preview_cart_with_item_elements_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().previewCartPage().getGoToCheckoutButton().isVisible()).isTrue();
            softly.assertThat(store().previewCartPage().getViewMyCartButton().isVisible()).isTrue();
            softly.assertThat(store().previewCartPage().getProductItemBlock().isVisible()).isTrue();
            softly.assertThat(store().previewCartPage().getSubtotalBlock().isVisible()).isTrue();
        });
    }

    @Test(description = "Increasing product quantity updates the product quantity value")
    public void should_increase_quantity_change_value_of_product_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();
        store().previewCartPage().increaseProductQuantityBy(1);

        Assertions.assertThat(store().previewCartPage().getProductQuantity().getAttribute("value")).isEqualTo("2");
    }

    @Test(description = "Reducing product quantity updates the product quantity value")
    public void should_reduce_quantity_change_value_of_product_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();
        store().previewCartPage().increaseProductQuantityBy(1);
        store().previewCartPage().reduceProductQuantityBy(1);

        Assertions.assertThat(store().previewCartPage().getProductQuantity().getAttribute("value")).isEqualTo("1");
    }

    @Test(description = "Reduce quantity button is disabled when product quantity is one")
    public void should_reduce_quantity_be_disabled_for_one_product_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();

        assertThat(store().previewCartPage().getReduceQuantityButton()).isDisabled();
    }

    @Test(description = "Removing an item opens the empty preview cart page")
    public void should_remove_item_open_empty_preview_cart_page_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();
        store().previewCartPage().removeItem();

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().previewCartPage().getEmptyCartText().isVisible()).isTrue();
            softly.assertThat(store().previewCartPage().getStartShoppingButton().isVisible()).isTrue();
        });
    }

    @Test(description = "Going to checkout redirects to the checkout page")
    public void should_go_to_checkout_redirect_to_checkout_page_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();
        store().previewCartPage().goToCheckout();

        assertThat(PlaywrightManager.getPage()).hasURL("http://localhost:8080/checkout/");
        assertThat(PlaywrightManager.getPage()).hasTitle("Checkout – Test App");
    }

    @Test(description = "View My Cart redirects to the cart page")
    public void should_view_my_cart_redirect_to_cart_page_test() {
        var dbTitle = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(dbTitle);
        store().previewCartPage().waitForPreviewCart();
        store().previewCartPage().viewMyCart();

        assertThat(PlaywrightManager.getPage()).hasURL("http://localhost:8080/cart/");
        assertThat(PlaywrightManager.getPage()).hasTitle("Cart – Test App");
    }

}

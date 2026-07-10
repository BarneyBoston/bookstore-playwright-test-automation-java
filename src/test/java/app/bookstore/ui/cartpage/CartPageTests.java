package app.bookstore.ui.cartpage;

import app.bookstore.api.coupon.PostCouponRequest;
import app.bookstore.db.BookStoreDB;
import app.bookstore.db.models.PostRecord;
import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.UiAssertions;
import app.bookstore.ui.helpers.navigation.AppPage;
import app.bookstore.ui.pages.CartPage;
import io.qameta.allure.Epic;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static app.bookstore.ui.helpers.UiAssertions.el;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Cart Page Tests")
public class CartPageTests extends BaseUiTest {

    @BeforeMethod(dependsOnMethods = "setUp")
    public void createCouponWithApi() {
        var request = PostCouponRequest.builder()
                .code("testCoupon")
                .discount_type("percent")
                .amount("5")
                .minimum_amount("1")
                .build();
        controller().postCoupon(request);
    }

    @Test(description = "Verify all essential UI elements are visible on the cart page.")
    public void should_cart_page_have_proper_elements_test() {
        store().mainPage().addRandomToCart();
        store().navigation().goTo(AppPage.CART);
        CartPage cartPage = store().cartPage();

        UiAssertions.assertAllVisible(
                el("Cart text", cartPage.getCartText()),
                el("Cart table", cartPage.getCartTable()),
                el("Cart totals table", cartPage.getCartTotalsTable()));
    }

    @Test(description = "Ensure product name in the cart matches the selected product.")
    public void should_product_name_match_chosen_product_test() {
        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(bookName);
        store().previewCartPage().waitForPreviewCart();
        store().navigation().goTo(AppPage.CART);

        CartPage cartPage = store().cartPage();
        assertThat(cartPage.getProductNames().nth(0).innerText()).isEqualTo(bookName);
    }

    @Test(description = "Ensure names of multiple products in the cart match the ones selected.")
    public void should_multiple_product_names_match_chosen_products_test() {
        var bookNames = BookStoreDB.getDb().selectActiveProducts().stream().map(PostRecord::getName).toList();
        var bookName1 = bookNames.getFirst();
        var bookName2 = bookNames.get(1);

        store().mainPage().addToCart(bookName1);
        store().previewCartPage().closePreview();
        store().mainPage().addToCart(bookName2);
        store().previewCartPage().waitForPreviewCart();
        store().navigation().goTo(AppPage.CART);

        assertThat(store().cartPage().getProductNames()).hasText(new String[]{bookName1, bookName2});
    }

    @Test(description = "Verify update cart triggers confirmation message after quantity change.")
    public void should_update_cart_trigger_pop_up_test() {
        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();

        store().mainPage().addToCart(bookName);
        store().previewCartPage().waitForPreviewCart();
        store().navigation().goTo(AppPage.CART);
        store().cartPage().setQuantityOfProductTo(0,3);
        store().cartPage().updateCart();

        assertThat(store().notifications().getSuccessMessage().innerText()).isEqualTo("Cart updated.");
    }
//
//    @Test(description = "Verify that 'Update Cart' button is disabled before any change.")
//    public void should_update_cart_without_prior_updated_be_disabled_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .assertUpdateCartButtonIsDisabled();
//    }
//
//    @Test(description = "Verify increasing quantity for one product is correctly reflected in the cart.")
//    public void should_increase_quantity_for_one_product_work_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .increaseQuantityOfProductBy(0, 2)
//                .updateCart()
//                .assertQuantityIs(0, "3");
//    }
//
//    @Test(description = "Verify increasing quantity for multiple products is correctly updated.")
//    public void should_increase_quantity_for_multiple_products_work_test() {
//        var bookNames = BookStoreDB.getDb().selectActiveProducts().stream().map(PostRecord::getName).toList();
//        var bookName1 = bookNames.get(0);
//        var bookName2 = bookNames.get(1);
//
//        mainPage
//                .addToCart(bookName1)
//                .closePreviewCart()
//                .addToCart(bookName2)
//                .closePreviewCart()
//                .getNavigationBar()
//                .clickCartPageButton()
//                .viewMyCart()
//                .increaseQuantityOfProductBy(0, 2)
//                .increaseQuantityOfProductBy(1, 4)
//                .updateCart()
//                .assertQuantityIs(0, "3")
//                .assertQuantityIs(1, "5");
//    }
//
//    @Test(description = "Verify reducing quantity for a single product updates correctly.")
//    public void should_reduce_quantity_for_one_product_work_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .increaseQuantityOfProductBy(0, 5)
//                .reduceQuantityOfProductBy(0, 2)
//                .updateCart()
//                .assertQuantityIs(0, "4");
//    }
//
//    @Test(description = "Verify reducing quantity for multiple products updates the cart correctly.")
//    public void should_reduce_quantity_for_multiple_products_work_test() {
//        var bookNames = BookStoreDB.getDb().selectActiveProducts().stream().map(PostRecord::getName).toList();
//        var bookName1 = bookNames.get(0);
//        var bookName2 = bookNames.get(1);
//
//        mainPage
//                .addToCart(bookName1)
//                .closePreviewCart()
//                .addToCart(bookName2)
//                .closePreviewCart()
//                .getNavigationBar()
//                .clickCartPageButton()
//                .viewMyCart()
//                .increaseQuantityOfProductBy(0, 2)
//                .reduceQuantityOfProductBy(0, 2)
//                .increaseQuantityOfProductBy(1, 4)
//                .reduceQuantityOfProductBy(1, 2)
//                .updateCart()
//                .assertQuantityIs(0, "1")
//                .assertQuantityIs(1, "3");
//    }
//
//    @Test(description = "Verify that entering a specific quantity directly into the input works as expected.")
//    public void should_input_quantity_for_product_work_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .inputQuantityAs(0, "55")
//                .updateCart()
//                .assertQuantityIs(0, "55");
//    }
//
//    @Test(description = "Verify that changing quantity updates the product subtotal correctly.")
//    public void should_update_quantity_update_subtotal_for_product_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .inputQuantityAs(0, "5")
//                .updateCart()
//                .assertSubTotalChangedToExpected(0);
//    }
//
//    @Test(description = "Ensure the total cart amount updates correctly after quantity changes.")
//    public void should_update_quantity_update_cart_total_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .inputQuantityAs(0, "5")
//                .updateCart()
//                .assertCartTotalsChangedAsExpected(0);
//    }
//
//    @Test(description = "Verify a correct coupon code is applied successfully.")
//    public void should_correct_coupon_work_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .inputCouponCodeAs("testCoupon")
//                .applyCoupon()
//                .assertMessageIs("Coupon code applied successfully.");
//    }
//
//    @Test(description = "Verify using an invalid coupon code results in appropriate error message.")
//    public void should_incorrect_coupon_work_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//        var couponCode = "incorrectCoupon";
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .inputCouponCodeAs(couponCode)
//                .applyCoupon()
//                .assertErrorIs(String.format("Coupon \"%s\" does not exist!", couponCode));
//    }
//
//    @Test(description = "Verify that clicking 'Proceed to Checkout' navigates to the checkout page.")
//    public void should_proceed_to_checkout_work_test() {
//        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
//
//        mainPage
//                .goToCartPageWithProductAs(bookName)
//                .proceedToCheckout()
//                .assertCheckoutTextIsDisplayed();
//    }
//
//    @AfterClass
//    public void cleanUp() {
//        var couponIds = controller.getCoupon().stream().map(CouponResponse::getId).toList();
//
//        if (!couponIds.isEmpty()) {
//            couponIds.forEach(coupon -> controller.deleteCouponResponse(coupon, true));
//        }
//    }
}

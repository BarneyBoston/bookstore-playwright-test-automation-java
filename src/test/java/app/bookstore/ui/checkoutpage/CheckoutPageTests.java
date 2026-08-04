package app.bookstore.ui.checkoutpage;

import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.navigation.AppPage;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


@Epic("Checkout Page Tests")
public class CheckoutPageTests extends BaseUiTest {

    @BeforeMethod
    public void goToCheckoutPage() {
        var bookName = BookStoreDB.getDb().selectRandomActiveProduct().getName();
        store().mainPage().addToCart(bookName);
        store().previewCartPage().waitForPreviewCart();

        store().navigation().goTo(AppPage.CHECKOUT);
    }

    @Test(description = "Verify that all essential elements are displayed on the checkout page")
    public void should_all_elements_be_displayed_test() {
        assertThat(store().checkoutPage().getBillingDetailsSection()).isVisible();
        assertThat(store().checkoutPage().getCheckoutOrderSection()).isVisible();
        assertThat(store().checkoutPage().getAdditionalInformationSection()).isVisible();
    }

    @Test(description = "Verify that clicking 'Click here to login' expands the login window")
    public void should_click_here_to_login_expand_login_window_test() {
        store().checkoutPage().clickHereToLogin();

        assertThat(store().checkoutPage().getUsernameOrEmailField()).isVisible();
        assertThat(store().checkoutPage().getPasswordField()).isVisible();
    }

    @Test(description = "Verify that clicking 'Click here to enter your code' expands the coupon code window")
    public void should_click_here_to_enter_your_code_expand_coupon_window_test() {
        store().checkoutPage().clickHereToEnterYourCode();

        assertThat(store().checkoutPage().getCouponCodeField()).isVisible();
        assertThat(store().checkoutPage().getApplyCouponButton()).isVisible();
    }

    @Test(description = "Verify that placing an order without required fields shows card error")
    public void should_place_order_without_required_fields_fail_test() {
        store().checkoutPage().placeOrder();
        store().checkoutPage().waitForOverlayToDisappear();

        Assertions.assertThat(store().checkoutPage().getCardErrorMessage().innerText()).isEqualTo("The card number is incomplete.");
    }

    @Test(description = "Verify placing order with card details but missing billing info shows appropriate errors")
    public void should_place_order_with_card_details_point_to_other_required_fields_test() {
        String expectedErrorMessage = """
                Billing First name is a required field.
                Billing Last name is a required field.
                Billing Street address is a required field.
                Billing Postcode / ZIP is not a valid postcode / ZIP.
                Billing Town / City is a required field.
                Billing Phone is a required field.
                Billing Email address is a required field.
                """.trim();

        store().checkoutPage().inputCardNumber("4242424242424242");
        store().checkoutPage().inputExpiryDate("12/58");
        store().checkoutPage().inputCvcNumber("123");
        store().checkoutPage().placeOrder();
        store().checkoutPage().waitForOverlayToDisappear();

        Assertions.assertThat(store().checkoutPage().getDetailsErrorMessage().innerText()).isEqualTo(expectedErrorMessage);
    }

    @Test(description = "Verify placing a correct order works successfully")
    public void should_place_correct_order_work_test() {
        store().checkoutPage().fillRandomOrderDetails();
        store().checkoutPage().waitForOverlayToDisappear();

        assertThat(store().checkoutPage().getOrderReceivedMessage()).isVisible();
    }
}
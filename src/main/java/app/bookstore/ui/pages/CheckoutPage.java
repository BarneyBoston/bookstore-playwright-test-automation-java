package app.bookstore.ui.pages;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
public class CheckoutPage extends BasePage {

    public CheckoutPage(Page page) {
        super(page);
    }

    // Headers
    private final Locator checkoutText = page.locator("//h1[text()='Checkout']");
    private final Locator orderReceivedMessage = page.locator("//h1[text()='Order received']");

    // Buttons / Links
    private final Locator clickHereToLoginButton = page.locator(".showlogin");
    private final Locator clickHereToEnterYourCodeButton = page.locator(".showcoupon");
    private final Locator applyCouponButton = page.locator("[name='apply_coupon']");
    private final Locator placeOrderButton = page.locator("#place_order");

    // Login
    private final Locator usernameOrEmailField = page.locator("#username");
    private final Locator passwordField = page.locator("#password");

    // Coupon
    private final Locator couponCodeField = page.locator("#coupon_code");

    // Sections
    private final Locator billingDetailsSection = page.locator(".woocommerce-billing-fields");
    private final Locator additionalInformationSection = page.locator(".woocommerce-additional-fields");
    private final Locator checkoutOrderSection = page.locator(".woocommerce-checkout-review-order");

    // Billing Details
    private final Locator firstNameField = page.locator("#billing_first_name");
    private final Locator lastNameField = page.locator("#billing_last_name");
    private final Locator houseNumberAndStreetNameField = page.locator("#billing_address_1");
    private final Locator apartmentSuiteUnitField = page.locator("#billing_address_2");
    private final Locator postcodeField = page.locator("#billing_postcode");
    private final Locator townField = page.locator("#billing_city");
    private final Locator phoneField = page.locator("#billing_phone");
    private final Locator emailField = page.locator("#billing_email");

    // Error messages
    private final Locator cardErrorMessage = page.locator(".woocommerce_error");
    private final Locator detailsErrorMessage = page.locator(".woocommerce-error");

    // Stripe iframes
    private final Locator cardNumberFrame = page.locator("[title='Secure card number input frame']");
    private final Locator expiryDateFrame = page.locator("[title='Secure expiration date input frame']");
    private final Locator cvcFrame = page.locator("[title='Secure CVC input frame']");

    // Overlay
    private final Locator overlay = page.locator("div.blockUI.blockOverlay");

    @Step("Click here to login")
    public void clickHereToLogin() {
        clickHereToLoginButton.click();
    }

    @Step("Click here to enter your code")
    public void clickHereToEnterYourCode() {
        clickHereToEnterYourCodeButton.click();
    }

    @Step("Place order")
    public void placeOrder() {
        placeOrderButton.click();
    }

    @Step("Input card number")
    public void inputCardNumber(String number) {
        cardNumberFrame
                .contentFrame()
                .locator("[name='cardnumber']")
                .fill(number);
    }

    @Step("Input expiry date")
    public void inputExpiryDate(String date) {
        expiryDateFrame
                .contentFrame()
                .locator("[name='exp-date']")
                .fill(date);
    }

    @Step("Input CVC number")
    public void inputCvcNumber(String number) {
        cvcFrame
                .contentFrame()
                .locator("[name='cvc']")
                .fill(number);
    }

    @Step("Input first name as")
    public void inputFirstNameAs(String name) {
        firstNameField.fill(name);
    }

    @Step("Input last name as")
    public void inputLastNameAs(String name) {
        lastNameField.fill(name);
    }

    @Step("Input house number and street name as")
    public void inputHouseNumberAndStreetNameAs(String name) {
        houseNumberAndStreetNameField.fill(name);
    }

    @Step("Input apartment suite unit as")
    public void inputApartmentSuiteUnitAs(String name) {
        apartmentSuiteUnitField.fill(name);
    }

    @Step("Input postcode as")
    public void inputPostcodeAs(String postcode) {
        postcodeField.fill(postcode);
    }

    @Step("Input town as")
    public void inputTownAs(String town) {
        townField.fill(town);
    }

    @Step("Input phone as")
    public void inputPhoneAs(String phone) {
        phoneField.fill(phone);
    }

    @Step("Input email as")
    public void inputEmailAs(String email) {
        emailField.fill(email);
    }

    @Step("Fill random order details")
    public void fillRandomOrderDetails() {
        //randomize test data creation
        Faker faker = new Faker();

        inputFirstNameAs(faker.name().firstName());
        inputLastNameAs(faker.name().lastName());
        inputHouseNumberAndStreetNameAs(faker.address().streetAddress());
        inputApartmentSuiteUnitAs(faker.address().streetAddressNumber());
        inputPostcodeAs(faker.address().postcode());
        inputTownAs(faker.address().city());
        inputPhoneAs(faker.phoneNumber().phoneNumber());
        inputEmailAs(faker.internet().emailAddress());
        inputCardNumber("4242424242424242");
        inputExpiryDate(LocalDate.now(ZoneId.of("CET")).plusMonths(faker.number().numberBetween(1, 60)).format(DateTimeFormatter.ofPattern("MM/yy")));
        inputCvcNumber(faker.idNumber().valid());
        placeOrder();
        waitForOverlayToDisappear();
    }

    @Step("Wait for overlay to disappear")
    public void waitForOverlayToDisappear() {
        page.waitForCondition(() -> !overlay.isVisible());
    }
}

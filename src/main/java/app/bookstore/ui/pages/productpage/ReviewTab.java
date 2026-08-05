package app.bookstore.ui.pages.productpage;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class ReviewTab extends BasePage {

    private final Locator reviewTitleText;
    private final Locator ratingStars;
    private final Locator reviewField;
    private final Locator nameField;
    private final Locator emailField;
    private final Locator submitButton;
    private final Locator errorMessage;
    private final Locator backFromErrorButton;
    private final Locator reviewAwaitingApprovalText;

    public ReviewTab(Page page) {
        super(page);
        this.reviewTitleText = this.page.locator(".woocommerce-Reviews-title");
        this.ratingStars = this.page.locator(".stars a");
        this.reviewField = this.page.locator("#comment");
        this.nameField = this.page.locator("#author");
        this.emailField = this.page.locator("#email");
        this.submitButton = this.page.locator("#submit");
        this.errorMessage = this.page.locator(".wp-die-message");
        this.backFromErrorButton = this.page.locator("xpath=//a[text()='« Back']");
        this.reviewAwaitingApprovalText = this.page.locator(".woocommerce-review__awaiting-approval");
    }

    @Step("Click submit button")
    public void clickSubmitButton() {
        submitButton.click();
    }

    @Step("Click back from error button")
    public void clickBackFromErrorButton() {
        backFromErrorButton.click();
        waitForNetworkIdle();
    }

    @Step("Choose rating from 1 to 5: {rating}")
    public void chooseRatingAs(Integer rating) {
        ratingStars.nth(rating).click();
    }

    @Step("Input name as {name}")
    public void inputNameAs(String name) {
        nameField.fill(name);
    }

    @Step("Input review as {review}")
    public void inputReviewAs(String review) {
        reviewField.fill(review);
    }

    @Step("Input email as {email}")
    public void inputEmailAs(String email) {
        emailField.fill(email);
    }
}

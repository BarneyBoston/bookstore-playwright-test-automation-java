package app.bookstore.ui.pages;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class MyAccountPage extends BasePage {

    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator lostPasswordLink;
    private final Locator rememberMeBox;
    private final Locator successfulLoginNoticeText;
    private final Locator incorrectLoginMessageText;

    public MyAccountPage(Page page) {
        super(page);
        this.usernameField = page.locator("#username");
        this.passwordField = page.locator("#password");
        this.loginButton = page.locator("[name='login']");
        this.lostPasswordLink = page.locator(".lost_password a");
        this.rememberMeBox = page.locator("#rememberme");
        this.successfulLoginNoticeText = page.locator(".woocommerce-notices-wrapper + p");
        this.incorrectLoginMessageText = page.locator(".woocommerce-error li");
    }

    @Step("Login as admin")
    public void loginAs(String username, String password) {
        usernameField.fill(username);
        passwordField.fill(password);
        loginButton.click();
    }

    @Step("Click lost your password link")
    public void lostYourPassword() {
        lostPasswordLink.click();
    }
}
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
        usernameField = page.locator("#username");
        passwordField = page.locator("#password");
        loginButton = page.locator("[name='login']");
        lostPasswordLink = page.locator(".lost_password a");
        rememberMeBox = page.locator("#rememberme");
        successfulLoginNoticeText = page.locator(".woocommerce-notices-wrapper + p");
        incorrectLoginMessageText = page.locator(".woocommerce-error li");
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
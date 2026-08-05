package app.bookstore.ui.pages;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;


@SuppressWarnings("unused")
@Getter
public class LostPasswordPage extends BasePage {

    private final Locator entryTitleText;
    private final Locator usernameOrEmailField;
    private final Locator resetPasswordButton;
    private final Locator errorMessageText;
    private final Locator successfulMessageText;

    public LostPasswordPage(Page page) {
        super(page);
        entryTitleText = page.locator(".entry-title");
        usernameOrEmailField = page.locator("#user_login");
        resetPasswordButton = page.locator("[value='Reset password']");
        errorMessageText = page.locator(".woocommerce-error li");
        successfulMessageText = page.locator(".woocommerce-message");
    }

    @Step("Input username or email as {username}")
    public void inputUsernameOrEmail(String username) {
        usernameOrEmailField.fill(username);
    }

    @Step("Click reset password")
    public void resetPassword() {
        resetPasswordButton.click();
    }
}

package app.bookstore.ui.pages.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

@Getter
public class Notifications extends BasePage {

    private final Locator successMessage;
    private final Locator errorMessage;

    public Notifications(Page page) {
        super(page);
        this.successMessage = page.locator(".woocommerce-message[role='alert']");
        this.errorMessage = page.locator(".woocommerce-error[role='alert']");
    }

}

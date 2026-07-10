package app.bookstore.ui.pages;

import app.bookstore.ui.helpers.UiAssertions;
import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

import static app.bookstore.ui.helpers.UiAssertions.el;

public class PreviewCartPage extends BasePage {

    @Getter
    private final Locator previewCartContext;
    private final Locator viewMyCartButton;
    private final Locator goToCheckoutButton;
    private final Locator closePreviewCartButton;

    public PreviewCartPage(Page page) {
        super(page);

        this.previewCartContext = page.locator(".wc-block-components-drawer__screen-overlay--with-slide-out");
        this.viewMyCartButton = page.locator("//*[text()='View my cart']");
        this.goToCheckoutButton = page.locator("//*[text()='Go to checkout']");
        this.closePreviewCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close mini cart"));
    }

    @Step("Wait for preview cart to show up")
    public void waitForPreviewCart(){
        UiAssertions.assertAllVisible(
                el("View My Cart button", viewMyCartButton),
                el("Go To Checkout button", goToCheckoutButton));
    }

    @Step("Close preview")
    public void closePreview(){
        closePreviewCartButton.click();
    }

}

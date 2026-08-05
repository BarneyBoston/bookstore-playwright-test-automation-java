package app.bookstore.ui.pages;

import app.bookstore.ui.helpers.UiAssertions;
import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

import java.util.stream.IntStream;

import static app.bookstore.ui.helpers.UiAssertions.el;

@Getter
@SuppressWarnings("unused")
public class PreviewCartPage extends BasePage {

    private final Locator previewCartContext;
    private final Locator viewMyCartButton;
    private final Locator goToCheckoutButton;
    private final Locator closePreviewCartButton;
    private final Locator emptyCartText;
    private final Locator startShoppingButton;
    private final Locator productItemBlock;
    private final Locator subtotalBlock;
    private final Locator increaseQuantityButton;
    private final Locator reduceQuantityButton;
    private final Locator productQuantity;
    private final Locator removeItemButton;

    public PreviewCartPage(Page page) {
        super(page);

        this.previewCartContext = page.locator(".wc-block-components-drawer__screen-overlay--with-slide-out");
        this.viewMyCartButton = page.locator("//*[text()='View my cart']");
        this.goToCheckoutButton = page.locator("//*[text()='Go to checkout']");
        this.closePreviewCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close mini cart"));
        this.emptyCartText = page.locator("//strong[text()='Your cart is currently empty!']");
        this.startShoppingButton = page.locator("//a[text()='Start shopping']");
        this.productItemBlock = page.locator(".wc-block-cart-items__row");
        this.subtotalBlock = page.locator(".wc-block-components-totals-item");
        this.increaseQuantityButton = page.locator("[aria-label='Increase quantity']");
        this.reduceQuantityButton = page.locator("[aria-label='Reduce quantity']");
        this.productQuantity = page.locator("[type='number']");
        this.removeItemButton = page.locator("//button[text()='Remove item']");
    }

    @Step("Wait for preview cart to show up")
    public void waitForPreviewCart() {
        UiAssertions.assertAllVisible(
                el("View My Cart button", viewMyCartButton),
                el("Go To Checkout button", goToCheckoutButton));
    }

    @Step("Close preview")
    public void closePreview() {
        closePreviewCartButton.click();
    }

    @Step("Click start shopping")
    public void clickStartShopping() {
        startShoppingButton.click();
    }

    @Step("Click increase product quantity by {number}")
    public void increaseProductQuantityBy(Integer number) {
        IntStream.range(0, number).forEach(i -> increaseQuantityButton.click());
    }

    @Step("Click reduce product quantity by {number}")
    public void reduceProductQuantityBy(Integer number) {
        IntStream.range(0, number).forEach(i -> reduceQuantityButton.click());
    }

    @Step("Click remove item")
    public void removeItem() {
        removeItemButton.click();
        waitForNetworkIdle();
    }

    @Step("Click view my cart")
    public void viewMyCart() {
        viewMyCartButton.click();
    }

    @Step("Click go to checkout")
    public void goToCheckout() {
        goToCheckoutButton.click();
    }

}

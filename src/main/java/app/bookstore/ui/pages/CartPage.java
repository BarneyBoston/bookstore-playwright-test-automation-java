package app.bookstore.ui.pages;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.IntStream;

@Getter
public class CartPage extends BasePage {

    private final Locator cartText;
    private final Locator cartTable;
    private final Locator cartTotalsTable;
    private final Locator productNames;
    private final Locator quantityFields;
    private final Locator updateCartButton;
    private final Locator cartTablePrices;
    private final Locator cartTableSubtotals;
    private final Locator cartTotalsPrices;
    private final Locator couponCodeTextArea;
    private final Locator applyCouponButton;
    private final Locator proceedToCheckoutButton;

    public CartPage(Page page) {
        super(page);

        this.cartText = page.locator("//h1[text()='Cart']");
        this.cartTable = page.locator(".shop_table.cart");
        this.cartTotalsTable = page.locator(".cart_totals");
        this.productNames = page.locator(".product-name>a");
        this.quantityFields = page.locator(".qty");
        this.updateCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update cart"));
        this.cartTablePrices = page.locator(".product-price .amount");
        this.cartTableSubtotals = page.locator(".cart_item [data-title='Subtotal'] .amount");
        this.cartTotalsPrices = page.locator(".cart_totals .amount");
        this.couponCodeTextArea = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Coupon:"));
        this.applyCouponButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply coupon"));
        this.proceedToCheckoutButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed to checkout"));
    }

    @Step("Set quantity of product at index {index} to {to}")
    public void setQuantityOfProductTo(int index, int to){
        Locator quantityField = quantityFields.nth(index);
        quantityField.fill(String.valueOf(to));
    }

    @Step("Increase quantity of product at index {index} by {by}")
    public void increaseQuantityOfProductBy(int index, int by) {
        Locator quantityField = quantityFields.nth(index);
        quantityField.waitFor();
        IntStream.range(0, by).forEach(i -> quantityField.press("ArrowUp"));
    }

    @Step("Decrease quantity of product at index {index} by {by}")
    public void decreaseQuantityOfProductBy(int index, int by) {
        Locator quantityField = quantityFields.nth(index);
        quantityField.waitFor();
        IntStream.range(0, by).forEach(i -> quantityField.press("ArrowDown"));
    }

    @Step("Update cart")
    public void updateCart(){
        updateCartButton.click();
    }

    @Step("Wait for cart page to fully load")
    public void waitForCartPage(){
        page.waitForCondition(updateCartButton::isDisabled);
    }

    @Step("Get quantity for product at index {index}")
    public Double getQuantityForProductAtIndex(int index){
        return Double.parseDouble(Objects.requireNonNull(quantityFields.nth(index).getAttribute("value")).trim());
    }

    @Step("Get price for product at index {index}")
    public Double getPriceForProductAtIndex(int index){
        return Double.parseDouble(cartTablePrices.nth(index).innerText().replaceAll("[^0-9.,]", "").replace(",", ".").trim());
    }

    @Step("Get subtotal for product at index {index}")
    public Double getSubtotalForProductAtIndex(int index){
        return Double.parseDouble(cartTableSubtotals.nth(index).innerText().replaceAll("[^0-9.,]", "").replace(",", ".").trim());
    }

    @Step("Get carts total for product at index {index}")
    public Double getCartsTotalForProductAtIndex(int index){
        return Double.parseDouble(cartTotalsPrices.nth(index).innerText().replaceAll("[^0-9.,]", "").replace(",", ".").trim());
    }

    @Step("Input coupon code as: {couponCode}")
    public void inputCouponCodeAs(String couponCode){
        couponCodeTextArea.fill(couponCode);
    }

    @Step("Apply coupon")
    public void applyCoupon(){
        applyCouponButton.click();
    }

    @Step("Proceed to checkout")
    public void proceedToCheckout(){
        proceedToCheckoutButton.click();
    }

}

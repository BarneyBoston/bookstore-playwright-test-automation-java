package app.bookstore.ui.pages;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class CartPage extends BasePage {

    private final Locator cartText;
    private final Locator cartTable;
    private final Locator cartTotalsTable;
    private final Locator productNames;
    private final Locator quantityFields;
    private final Locator updateCartButton;

    public CartPage(Page page) {
        super(page);

        this.cartText = page.locator("//h1[text()='Cart']");
        this.cartTable = page.locator(".shop_table.cart");
        this.cartTotalsTable = page.locator(".cart_totals");
        this.productNames = page.locator(".product-name>a");
        this.quantityFields = page.locator(".qty");
        this.updateCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update cart"));
    }

    @Step("Set quantity of product at index {index} to {to}")
    public void setQuantityOfProductTo(int index, int to){
        Locator quantityField = quantityFields.nth(index);
        quantityField.fill(String.valueOf(to));
    }

    @Step("Update cart")
    public void updateCart(){
        updateCartButton.click();
    }

}

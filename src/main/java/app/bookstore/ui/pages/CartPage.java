package app.bookstore.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

@Getter
public class CartPage extends BasePage {

    private final Locator cartText;
    private final Locator cartTable;
    private final Locator cartTotalsTable;
    private final Locator productNames;

    public CartPage(Page page) {
        super(page);

        this.cartText = page.locator("//h1[text()='Cart']");
        this.cartTable = page.locator(".shop_table.cart");
        this.cartTotalsTable = page.locator(".cart_totals");
        this.productNames = page.locator(".product-name>a");
    }


}

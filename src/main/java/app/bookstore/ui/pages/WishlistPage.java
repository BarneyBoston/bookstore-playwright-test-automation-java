package app.bookstore.ui.pages;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

@SuppressWarnings("unused")
@Getter
public class WishlistPage extends BasePage {

    private final Locator wishlistTitle;
    private final Locator productNames;
    private final Locator emptyWishlistText;

    public WishlistPage(Page page) {
        super(page);
        this.wishlistTitle = this.page.locator(".wishlist-title");
        this.productNames = this.page.locator("tr[id] .product-name");
        this.emptyWishlistText = this.page.locator(".wishlist-empty");
    }

    public Locator getProductName(String productName) {
        return this.page.locator(String.format("xpath=//tr[@id]//td[contains(@class, 'product-name')]//a[contains(text(), '%s')]", productName));
    }

    public Locator getAddToCartByProduct(String product) {
        return this.page.locator(String.format("a[aria-label='Add “%s” to your cart']", product));
    }

    public void clickChosenProduct(String productName) {
        getProductName(productName).click();
    }

    public void clickAddToCartByProduct(String productName) {
        getAddToCartByProduct(productName).click();
    }

}

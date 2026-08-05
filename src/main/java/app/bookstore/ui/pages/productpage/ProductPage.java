package app.bookstore.ui.pages.productpage;

import app.bookstore.ui.pages.helpers.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

import java.util.stream.IntStream;

@Getter
@SuppressWarnings("unused")
public class ProductPage extends BasePage {

    private final Locator addToCartButton;
    private final Locator viewCartPopUp;
    private final Locator viewCartButton;
    private final Locator quantityField;
    private final Locator addToWishListButton;
    private final Locator viewWishListButton;
    private final Locator removeFromWishListButton;
    private final Locator descriptionTabButton;
    private final Locator contentsTabButton;
    private final Locator reviewsTabButton;
    private final Locator relatedProductsSection;
    private final Locator descriptionTabText;
    private final Locator contentsTabSection;
    private final Locator productText;

    public ProductPage(Page page) {
        super(page);

        this.addToCartButton = page.locator("xpath=//button[text()='Add to cart']");
        this.viewCartPopUp = page.locator("[role='alert']");
        this.viewCartButton = page.locator("xpath=//a[text()='View cart']");
        this.quantityField = page.locator("[title='Qty']");
        this.addToWishListButton = page.locator("xpath=//span[text()='Add to wishlist']");
        this.viewWishListButton = page.locator(".view-wishlist");
        this.removeFromWishListButton = page.locator(".delete_item");
        this.descriptionTabButton = page.locator(".description_tab");
        this.contentsTabButton = page.locator(".contents_tab");
        this.reviewsTabButton = page.locator(".reviews_tab");
        this.relatedProductsSection = page.locator("[class='related products']");
        this.descriptionTabText = page.locator("#tab-description p");
        this.contentsTabSection = page.locator("[id='tab-contents']");
        this.productText = page.locator(".product_title.entry-title");
    }

    @Step("Go to review tab with button")
    public void goToReviewTabWithButton() {
        reviewsTabButton.click();
    }

    @Step("Click add product to cart")
    public void addProductToCart() {
        addToCartButton.click();
    }

    @Step("Click view cart from notification")
    public void viewCart() {
        viewCartButton.click();
    }

    @Step("Input quantity as: {quantity}")
    public void inputQuantityAs(String quantity) {
        quantityField.fill(quantity);
    }

    @Step("Increase quantity by: {by}")
    public void increaseQuantityBy(Integer by) {
        quantityField.waitFor();
        IntStream.range(0, by).forEach(i -> quantityField.press("ArrowUp"));
    }

    @Step("Decrease quantity by: {by}")
    public void decreaseQuantityBy(Integer by) {
        quantityField.waitFor();
        IntStream.range(0, by).forEach(i -> quantityField.press("ArrowDown"));
    }

    @Step("Click add to wishlist")
    public void addToWishList() {
        addToWishListButton.click();
    }

    @Step("Click view to wishlist")
    public void viewWishList() {
        viewWishListButton.click();
    }

    @Step("Click remove from wishlist")
    public void removeFromWishList() {
        removeFromWishListButton.click();
    }

    @Step("Click contents tab")
    public void clickContentsTab() {
        contentsTabButton.click();
    }
}

package app.bookstore.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import io.qameta.allure.Step;

import java.util.List;

public class MainPage extends BasePage {

    private final Locator searchInput;
    private final Locator submitButton;
    private final Locator productTitles;
    private final Locator sortDropdown;
    private final Locator priceContainers;

    public MainPage(Page page) {
        super(page);

        this.searchInput = page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("Search"));
        this.submitButton = page.getByRole(AriaRole.BUTTON,  new Page.GetByRoleOptions().setName("Search"));
        this.productTitles = page.locator(".woocommerce-loop-product__title");
        this.sortDropdown = page.getByLabel("Shop order");
        this.priceContainers = page.locator("[class='price']");

    }

    @Step("Search for product {product}")
    public void searchForProduct(String productName) {
        searchInput.click();
        searchInput.fill(productName);
        submitButton.click();
    }

    @Step("Get all product titles")
    public List<String> getProductTitles() {
        return productTitles.allInnerTexts();
    }

    @Step("Select sorting option as {optionText}")
    public void selectSortingOption(String optionText) {
        sortDropdown.selectOption(new SelectOption().setLabel(optionText));
        waitForNetworkIdle();
    }

    @Step("Get all product prices")
    public List<Double> getPrices() {
        return priceContainers.all().stream()
                .map(priceElement -> {
                    Locator discountedPrices = priceElement.locator("ins");
                    if (discountedPrices.count() > 0) {
                        return discountedPrices.innerText();
                    } else {
                        return priceElement.innerText();
                    }
                })
                .map(price -> price.replace("€", "").replace(",", ".").replace("\u00a0", "").trim())
                .map(Double::parseDouble)
                .toList();
    }

    @Step("Add to cart book named {bookName}")
    public void addToCart(String bookName) {
        page.locator("#main").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions()
                .setName(String.format("Add “%s” to your cart", bookName))).click();
        waitForPageToLoad();
    }
}

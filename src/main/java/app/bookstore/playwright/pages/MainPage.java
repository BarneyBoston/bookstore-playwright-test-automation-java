package app.bookstore.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MainPage {
    private final Page page;

    private final Locator searchInput;
    private final Locator submitButton;

    public MainPage(Page page) {
        this.page = page;

        this.searchInput = page.locator("[type='search']");
        this.submitButton = page.locator("[type='submit']");
    }

    public void searchForProduct(String productName) {
        searchInput.click();
        searchInput.fill(productName);
        submitButton.click();
    }
}

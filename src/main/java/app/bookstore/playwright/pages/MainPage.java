package app.bookstore.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MainPage {
    private final Page page;

    private final Locator searchInput;
    private final Locator searchButton;

    public MainPage(Page page) {
        this.page = page;

        this.searchInput = page.locator("input[placeholder='Search...']");
        this.searchButton = page.locator("button.search-submit");
    }

    public void searchForProduct(String productName) {
        searchInput.fill(productName);
        searchButton.click();
    }
}

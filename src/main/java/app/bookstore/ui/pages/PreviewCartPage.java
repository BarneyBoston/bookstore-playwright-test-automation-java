package app.bookstore.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

public class PreviewCartPage extends BasePage {

    @Getter
    private final Locator previewCartContext;

    public PreviewCartPage(Page page) {
        super(page);

        previewCartContext = page.locator(".wc-block-components-drawer__screen-overlay--with-slide-out");
    }

}

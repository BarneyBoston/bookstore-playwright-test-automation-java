package app.bookstore.ui.pages.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public class NavigationBar extends BasePage {

    private final Locator cartPageButton;
    private final Locator myAccountPageButton;
    private final Locator wishlistPageButton;

    public NavigationBar(Page page) {
        super(page);

        this.cartPageButton = page.locator(".wc-block-mini-cart__button");
        this.myAccountPageButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("My account"));
        this.wishlistPageButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Wishlist").setExact(true));
    }


}

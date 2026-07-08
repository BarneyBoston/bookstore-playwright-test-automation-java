package app.bookstore.ui.helpers.navigation;

public enum AppPage {
    HOME("/"),
    MY_ACCOUNT("/my-account"),
    LOST_PASSWORD("/my-account/lost-password/"),
    WISHLIST("/wishlist/"),
    PRODUCT("/product/%s"),
    COUPON("/coupons"),
    CART("/cart");

    private final String pathTemplate;

    AppPage(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public String path(Object... params) {
        return String.format(pathTemplate, params);
    }
}

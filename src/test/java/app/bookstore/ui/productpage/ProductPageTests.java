package app.bookstore.ui.productpage;

import app.bookstore.api.product.ProductReviewResponse;
import app.bookstore.db.BookStoreDB;
import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.PlaywrightManager;
import app.bookstore.ui.helpers.navigation.AppPage;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Product Page Tests")
public class ProductPageTests extends BaseUiTest {

    private String getRandomBookSlug() {
        return BookStoreDB.getDb().selectRandomActiveProduct().getSlug();
    }

    @Test(description = "Verify product page displays all expected elements")
    public void should_product_page_has_all_elements_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().productPage().getContentsTabButton().isVisible()).isTrue();
            softly.assertThat(store().productPage().getReviewsTabButton().isVisible()).isTrue();
            softly.assertThat(store().productPage().getRelatedProductsSection().isVisible()).isTrue();
        });
    }

    @Test(description = "Verify product title is correct after navigation")
    public void should_verify_product_title_after_navigation_test() {
        var book = BookStoreDB.getDb().selectActiveProducts().getLast();
        var bookSlug = book.getSlug();
        var bookName = book.getName();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);

        Assertions.assertThat(store().productPage().getProductText().innerText()).isEqualTo(bookName);
    }

    @Test(description = "Adding product to cart triggers notification popup with product name")
    public void should_add_to_cart_trigger_notification_test() {
        var book = BookStoreDB.getDb().selectActiveProducts().getLast();
        var bookSlug = book.getSlug();
        var bookName = book.getName();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().addProductToCart();


        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().productPage().getViewCartPopUp().isVisible()).isTrue();
            softly.assertThat(store().productPage().getViewCartPopUp().innerText()).contains(bookName);
        });
    }

    @Test(description = "Clicking view cart from notification redirects to cart page")
    public void should_go_to_view_cart_from_notification_redirect_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().addProductToCart();
        store().productPage().viewCart();

        assertThat(PlaywrightManager.getPage()).hasURL("http://localhost:8080/cart/");
        assertThat(PlaywrightManager.getPage()).hasTitle("Cart – Test App");
    }

    @Test(description = "Inputting quantity updates the quantity value")
    public void should_input_quantity_as_update_value_test() {
        int randomQuantity = ThreadLocalRandom.current().nextInt(2, 11);
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().inputQuantityAs(String.valueOf(randomQuantity));
        store().productPage().addProductToCart();

        Assertions.assertThat(store().productPage().getQuantityField().getAttribute("value")).isEqualTo(String.valueOf(randomQuantity));
    }

    @Test(description = "Increasing quantity updates the quantity value accordingly")
    public void should_increase_quantity_update_value_test() {
        int increaseAmount = ThreadLocalRandom.current().nextInt(1, 6);
        int expectedQuantity = 1 + increaseAmount;
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().increaseQuantityBy(increaseAmount);
        store().productPage().addProductToCart();

        Assertions.assertThat(store().productPage().getQuantityField().getAttribute("value")).isEqualTo(String.valueOf(expectedQuantity));
    }

    @Test(description = "Decreasing quantity updates the quantity value accordingly")
    public void should_decrease_quantity_update_value_test() {
        int inputQuantity = ThreadLocalRandom.current().nextInt(5, 11);
        int decreaseAmount = ThreadLocalRandom.current().nextInt(1, inputQuantity);
        int expectedQuantity = inputQuantity - decreaseAmount;
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().inputQuantityAs(String.valueOf(inputQuantity));
        store().productPage().decreaseQuantityBy(decreaseAmount);
        store().productPage().addProductToCart();

        Assertions.assertThat(store().productPage().getQuantityField().getAttribute("value")).isEqualTo(String.valueOf(expectedQuantity));
    }

    @Test(description = "Adding product to wishlist and verifying it appears there")
    public void should_add_to_wish_list_work_test() {
        var book = BookStoreDB.getDb().selectActiveProducts().getLast();
        var bookSlug = book.getSlug();
        var bookName = book.getName();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().addToWishList();
        store().productPage().viewWishList();

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().wishlistPage().getWishlistTitle().innerText().trim()).isEqualTo("My wishlist");
            softly.assertThat(store().wishlistPage().getProductNames().nth(0).innerText()).isEqualTo(bookName);
        });
    }

    @Test(description = "Removing product from wishlist and verifying wishlist is empty")
    public void should_remove_from_wish_list_work_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().addToWishList();
        store().productPage().removeFromWishList();
        store().navigationBar().goToWishlist();

        Assertions.assertThat(store().wishlistPage().getEmptyWishlistText().innerText()).isEqualTo("No products added to the wishlist");
    }

    @Test(description = "Contents tab displays its content when clicked")
    public void should_contents_tab_content_be_displayed_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().clickContentsTab();

        Assertions.assertThat(store().productPage().getContentsTabSection().isVisible()).isTrue();
    }

    @Test(description = "Review tab displays all necessary elements")
    public void should_review_tab_display_all_elements_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();

        SoftAssertions.assertSoftly(softly ->
        {
            softly.assertThat(store().reviewTab().getReviewTitleText().innerText()).as("Review title text is not as expected").isEqualTo("Reviews");
            softly.assertThat(store().reviewTab().getRatingStars().count()).as("There should be 5 rating stars").isEqualTo(5);
            softly.assertThat(store().reviewTab().getReviewField().isVisible()).as("Review field is not displayed and editable").isTrue();
            softly.assertThat(store().reviewTab().getNameField().isVisible()).as("Name field is not displayed and editable").isTrue();
            softly.assertThat(store().reviewTab().getEmailField().isVisible()).as("Email field is not displayed and editable").isTrue();
        });
    }

    @Test(description = "Submitting review with empty rating triggers alert")
    public void should_review_tab_empty_submit_trigger_pop_up_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();

        PlaywrightManager.getPage().onDialog(dialog -> {
            Assertions.assertThat(dialog.message()).isEqualTo("Please select a rating");
            dialog.accept();
        });
        store().reviewTab().clickSubmitButton();
    }

    @Test(description = "Submitting review with rating but missing required fields shows error")
    public void should_review_tab_with_rating_submit_redirect_to_error_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();
        store().reviewTab().chooseRatingAs(3);
        store().reviewTab().clickSubmitButton();

        Assertions.assertThat(store().reviewTab().getErrorMessage().innerText()).isEqualTo("Error: Please fill the required fields.");
    }

    @Test(description = "Submitting review with rating and back button redirects to description tab")
    public void should_review_tab_with_rating_submit_and_back_redirect_to_description_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();
        store().reviewTab().chooseRatingAs(3);
        store().reviewTab().clickSubmitButton();
        store().reviewTab().clickBackFromErrorButton();

        Assertions.assertThat(store().productPage().getDescriptionTabText().innerText()).isNotEmpty();
    }

    @Test(description = "Submitting review without name triggers proper error popup")
    public void should_review_tab_without_name_submit_popup_proper_error_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();
        store().reviewTab().chooseRatingAs(3);
        store().reviewTab().inputEmailAs("testUser@test.pl");
        store().reviewTab().clickSubmitButton();


        Assertions.assertThat(store().reviewTab().getErrorMessage().innerText()).isEqualTo("Error: Please fill the required fields.");
    }

    @Test(description = "Submitting review with incorrect email triggers error popup")
    public void should_review_tab_with_incorrect_email_submit_popup_proper_error_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();
        store().reviewTab().chooseRatingAs(3);
        store().reviewTab().inputReviewAs("Great book");
        store().reviewTab().inputNameAs("testUser");
        store().reviewTab().inputEmailAs("incorrectEmail");
        store().reviewTab().clickSubmitButton();

        Assertions.assertThat(store().reviewTab().getErrorMessage().innerText()).isEqualTo("Error: Please enter a valid email address.");
    }

    @Test(description = "Submitting review with correct details adds review and awaits approval")
    public void should_review_tab_with_correct_details_submitted_add_review_test() {
        var bookSlug = getRandomBookSlug();

        store().navigation().goTo(AppPage.PRODUCT, bookSlug);
        store().productPage().goToReviewTabWithButton();
        store().reviewTab().chooseRatingAs(3);
        store().reviewTab().inputReviewAs("Great book" + ((new Random().nextInt() * 900) + 100));
        store().reviewTab().inputNameAs("testUser");
        store().reviewTab().inputEmailAs("testUser@test.pl");
        store().reviewTab().clickSubmitButton();

        Assertions.assertThat(store().reviewTab().getReviewAwaitingApprovalText().innerText()).contains("Your review is awaiting approval");

    }

    @AfterMethod
    public void cleanUp() {
        var reviewIds = controller().getReviews().stream().map(ProductReviewResponse::getId).toList();

        if (!reviewIds.isEmpty()) {
            reviewIds.forEach(reviewId -> controller().deleteReviewsResponse(reviewId));
        }
    }
}

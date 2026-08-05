package app.bookstore.ui.myaccountpage;

import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.navigation.AppPage;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("My Account Page Tests")
public class MyAccountPageTests extends BaseUiTest {

    @Test(description = "Verify all elements on My Account page are visible")
    public void should_all_elements_be_visible_test() {
        store().navigation().goTo(AppPage.MY_ACCOUNT);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(store().myAccountPage().getUsernameField().isEnabled()).isTrue();
            softly.assertThat(store().myAccountPage().getPasswordField().isEnabled()).isTrue();
            softly.assertThat(store().myAccountPage().getLoginButton().isEnabled()).isTrue();
            softly.assertThat(store().myAccountPage().getLostPasswordLink().isEnabled()).isTrue();
            softly.assertThat(store().myAccountPage().getRememberMeBox().isEnabled()).isTrue();
        });
    }

    @Test(description = "Verify correct login works and success message contains username")
    public void should_correct_login_work_test() {
        store().navigation().goTo(AppPage.MY_ACCOUNT);
        store().myAccountPage().loginAs("admin", "admin");

        Assertions.assertThat(store().myAccountPage().getSuccessfulLoginNoticeText().innerText()).contains("Hello admin");
    }

    @Test(description = "Verify incorrect login triggers appropriate error message")
    public void should_incorrect_login_trigger_error_test() {
        store().navigation().goTo(AppPage.MY_ACCOUNT);
        store().myAccountPage().loginAs("incorrectLogin", "incorrectPassword");

        Assertions.assertThat(store().myAccountPage().getIncorrectLoginMessageText().innerText()).contains("Error: The username incorrectLogin is not registered on this site");
    }

    @Test(description = "Verify 'Lost your password' link redirects and displays expected elements")
    public void should_lost_your_password_redirect_test() {
        store().navigation().goTo(AppPage.MY_ACCOUNT);
        store().myAccountPage().lostYourPassword();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(store().lostPasswordPage().getUsernameOrEmailField().isEnabled()).isTrue();
            softly.assertThat(store().lostPasswordPage().getResetPasswordButton().isEnabled()).isTrue();
            softly.assertThat(store().lostPasswordPage().getEntryTitleText().innerText()).isEqualTo("Lost password");
        });
    }
}

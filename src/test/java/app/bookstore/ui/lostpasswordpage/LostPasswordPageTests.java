package app.bookstore.ui.lostpasswordpage;

import app.bookstore.ui.BaseUiTest;
import app.bookstore.ui.helpers.navigation.AppPage;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Lost Password Page Tests")
public class LostPasswordPageTests extends BaseUiTest {

    @Test(description = "Verify that Lost Password page displays all expected elements")
    public void should_lost_password_page_have_all_expected_elements_test() {
        SoftAssertions softAssertions = new SoftAssertions();

        store().navigation().goTo(AppPage.LOST_PASSWORD);

        softAssertions.assertThat(store().lostPasswordPage().getEntryTitleText().innerText())
                .isEqualTo("Lost password");
        softAssertions.assertThat(store().lostPasswordPage().getUsernameOrEmailField().isEnabled())
                .isTrue();
        softAssertions.assertThat(store().lostPasswordPage().getResetPasswordButton().isEnabled())
                .isTrue();

        softAssertions.assertAll();
    }

    @Test(description = "Verify that submitting incorrect username or email shows error message")
    public void should_incorrect_username_or_email_fail_test() {

        store().navigation().goTo(AppPage.LOST_PASSWORD);
        store().lostPasswordPage().inputUsernameOrEmail("incorrect username");
        store().lostPasswordPage().resetPassword();

        Assertions.assertThat(store().lostPasswordPage().getErrorMessageText().innerText()).isEqualTo("Invalid username or email.");
    }

    @Test(description = "Verify that submitting correct username or email shows success message")
    public void should_correct_username_or_email_work_test() {

        store().navigation().goTo(AppPage.LOST_PASSWORD);
        store().lostPasswordPage().inputUsernameOrEmail("admin");
        store().lostPasswordPage().resetPassword();

        Assertions.assertThat(store().lostPasswordPage().getSuccessfulMessageText().innerText()).isEqualTo("Password reset email has been sent.");
    }

}

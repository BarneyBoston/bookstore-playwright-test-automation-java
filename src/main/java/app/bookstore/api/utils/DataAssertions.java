package app.bookstore.api.utils;

import io.qameta.allure.Step;
import org.assertj.core.api.AssertionsForInterfaceTypes;

import java.util.List;

public abstract class DataAssertions {
    private DataAssertions() {
        /* This utility class should not be instantiated */
    }

    @Step("Verify that database records contains API response")
    public static <T> void verifyThatAPIvsDBListContains(List<T> responseList, List<T> dbList){
        AssertionsForInterfaceTypes
                .assertThat(dbList)
                .describedAs("Mismatch between API response data and database records.")
                .containsAll(responseList);
    }
}

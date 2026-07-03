package app.bookstore.ui.helpers;

import com.microsoft.playwright.Locator;
import org.assertj.core.api.SoftAssertions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UiAssertions {

        public record NamedLocator(String name, Locator locator) {}

        public static NamedLocator el(String name, Locator locator) {
            return new NamedLocator(name, locator);
        }

        public static void assertAllVisible(NamedLocator... elements) {
            SoftAssertions softly = new SoftAssertions();
            for (NamedLocator element : elements) {
                try {
                    assertThat(element.locator()).isVisible();
                } catch (AssertionError e) {
                    softly.fail("Element '%s' should be visible but wasn't", element.name());
                }
            }
            softly.assertAll();
        }

}

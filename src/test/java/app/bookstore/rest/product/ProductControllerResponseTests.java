package app.bookstore.rest.product;

import app.bookstore.rest.base.BaseRestTest;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Epic("Product Controller Response Tests")
public class ProductControllerResponseTests extends BaseRestTest {

    @Test(description = "Verify GET /products endpoint returns status 200")
    public void getProductResponseTest() {
        var response = controller().getProductsResponse();

        Assertions.assertThat(response.status()).isEqualTo(200);
    }

}
package app.bookstore.rest.product;

import app.bookstore.api.product.ProductRequest;
import app.bookstore.api.product.ProductResponse;
import app.bookstore.db.BookStoreDB;
import app.bookstore.db.models.ProductRecord;
import app.bookstore.rest.base.BaseRestTest;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@Epic("Product Controller Response Tests")
public class ProductControllerResponseTests extends BaseRestTest {

    @Test(description = "Verify GET /products endpoint returns status 200")
    public void getProductResponseTest() {
        var response = controller().getProductsResponse();

        Assertions.assertThat(response.status()).isEqualTo(200);
    }

    @Test(description = "Verify POST /products endpoint returns status 201 on creating product")
    public void postProductResponseTest() {
        var request = ProductRequest.builder()
                .build();
        var response = controller().postProductsResponse(request);

        Assertions.assertThat(response.status())
                .isEqualTo(201);
    }

    @Test(description = "Verify PUT /products/{id} endpoint returns status 200 on updating product")
    public void updateProductResponseTest() {
        var idFromDb = BookStoreDB.getDb().selectProducts().stream()
                .map(ProductRecord::getProductId)
                .toList()
                .getFirst();

        var request = ProductRequest.builder()
                .regularPrice("12.00")
                .build();

        var response = controller().updateProductsResponse(idFromDb.toString(), request);

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @Test(description = "Verify DELETE /products/{id} endpoint returns status 200 on deleting product")
    public void deleteProductResponseTest() {
        var request = ProductRequest.builder()
                .build();

        var id = controller().postProducts(request).getId();

        var response = controller().deleteProductsResponse(id.toString());

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @AfterClass
    public void clear() {
        try {
            var productIds = controller().getProducts()
                    .stream()
                    .filter(element -> element.getName().equals("NAME") || element.getName().equals("Product") || element.getName().equals("New Book"))
                    .map(ProductResponse::getId)
                    .toList();
            productIds.forEach(productId -> controller().deleteProductsResponse(productId.toString()));
        } catch (NullPointerException _) {
            log.info("Nothing to clear");
        }
    }

}
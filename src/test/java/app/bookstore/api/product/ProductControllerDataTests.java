package app.bookstore.api.product;

import app.bookstore.api.BaseApiTest;
import app.bookstore.api.utils.DataAssertions;
import app.bookstore.api.utils.DataTestParameters;
import app.bookstore.api.utils.MatrixCreator;
import app.bookstore.db.BookStoreDB;
import app.bookstore.db.models.PostRecord;
import app.bookstore.db.models.ProductRecord;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ProductControllerDataTests extends BaseApiTest {
    private ProductControllerParametersFactory parametersFactory;

    @BeforeClass
    public void setupFactory() {
        parametersFactory = new ProductControllerParametersFactory();
    }

    @DataProvider
    public Object[][] productData() {
        return MatrixCreator.arrayToMatrix(
                parametersFactory.productId(),
                parametersFactory.minPrice(),
                parametersFactory.maxPrice()
        );
    }

    @Test(dataProvider = "productData", description = "Verify product data from API matches product data from DB")
    public <T> void productDataTest(DataTestParameters<ProductResponse, ProductRecord, T> parameters) {
        var responseList = controller().getProducts().stream()
                .map(parameters.responseMap())
                .toList();

        var productList = BookStoreDB.getDb().selectProducts().stream()
                .map(parameters.dbMap())
                .toList();

        DataAssertions.verifyThatAPIvsDBListContains(responseList, productList);
    }

    @DataProvider
    public Object[][] postData() {
        return MatrixCreator.arrayToMatrix(
                parametersFactory.name(),
                parametersFactory.postStatus(),
                parametersFactory.slug(),
                parametersFactory.postDate()
        );
    }

    @Test(dataProvider = "postData", description = "Verify posted product data from API matches post records from DB")
    public <T> void postDataTest(DataTestParameters<ProductResponse, PostRecord, T> parameters) {
        var responseList = controller().getProducts().stream()
                .map(parameters.responseMap())
                .toList();

        var productList = BookStoreDB.getDb().selectPosts().stream()
                .map(parameters.dbMap())
                .toList();

        DataAssertions.verifyThatAPIvsDBListContains(responseList, productList);
    }

    @Test(description = "Verify that posting a product with name 'NAME' matches the DB record")
    public void postProductNameTest() {
        var request = ProductRequest.builder()
                .name("NAME")
                .build();
        var responseName = controller().postProducts(request).getName();

        var dbName = BookStoreDB.getDb().selectNameFromPosts("NAME")
                .stream()
                .map(PostRecord::getName)
                .toList()
                .getFirst();

        AssertionsForInterfaceTypes.assertThat(responseName).isEqualTo(dbName);
    }

    @Test(description = "Verify that updating a product price updates the price correctly")
    public void updateProductDataTest() {
        var idFromDb = BookStoreDB.getDb().selectProducts().stream()
                .map(ProductRecord::getProductId)
                .toList()
                .getFirst();

        var request = ProductRequest.builder()
                .regularPrice("12.0")
                .build();

        var response = controller().updateProduct(idFromDb.toString(), request);

        AssertionsForInterfaceTypes.assertThat(response.getPrice()).isEqualTo(13.0);
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
        } catch (Exception _) {
            log.info("Skipping due to lack of orderIds to clean");
        }
    }
}

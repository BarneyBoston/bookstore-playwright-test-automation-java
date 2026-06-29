package app.bookstore.api;

import app.bookstore.api.product.ProductRequest;
import app.bookstore.api.product.ProductResponse;
import app.bookstore.helpers.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Step;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.util.List;

public class BookStoreApiController {

    private final APIRequestContext request;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String consumerKey;
    private final String consumerSecret;
    private final String baseUri;

    private static final String PRODUCTS = "/products";
    private static final String COUPONS = "/coupons";
    private static final String CUSTOMERS = "/customers";
    private static final String ORDERS = "/orders";
    private static final String REVIEWS = "/products/reviews";

    public BookStoreApiController(APIRequestContext request) {
        Config config = Config.getInstance();
        this.request = request;
        this.consumerKey = config.getConsumerKey();
        this.consumerSecret = config.getConsumerSecret();
        this.baseUri = config.getBaseUri();
    }

    private String getAuthHeader(String method, String url) throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        return switch (method.toUpperCase()) {
            case "GET" -> {
                HttpGet req = new HttpGet(url);
                consumer.sign(req);
                yield req.getFirstHeader("Authorization").getValue();
            }
            case "POST" -> {
                HttpPost req = new HttpPost(url);
                consumer.sign(req);
                yield req.getFirstHeader("Authorization").getValue();
            }
            case "PUT" -> {
                HttpPut req = new HttpPut(url);
                consumer.sign(req);
                yield req.getFirstHeader("Authorization").getValue();
            }
            case "DELETE" -> {
                HttpDelete req = new HttpDelete(url);
                consumer.sign(req);
                yield req.getFirstHeader("Authorization").getValue();
            }
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };
    }

    private String auth(String method, String endpoint) {
        try {
            return getAuthHeader(method, baseUri + endpoint);
        } catch (Exception e) {
            throw new RuntimeException("OAuth signing failed for " + method + " " + endpoint, e);
        }
    }

    // ── PRODUCTS ──────────────────────────────────────────

    @Step("GET " + PRODUCTS)
    public APIResponse getProductsResponse() {
        return request.get(baseUri + PRODUCTS, RequestOptions.create()
                .setHeader("Authorization", auth("GET", PRODUCTS)));
    }

    public List<ProductResponse> getProducts() {
        return deserializeList(getProductsResponse(), ProductResponse.class);
    }

    @Step("POST " + PRODUCTS)
    public APIResponse postProductsResponse(ProductRequest body) {
        return request.post(baseUri + PRODUCTS, RequestOptions.create()
                .setHeader("Authorization", auth("POST", PRODUCTS))
                .setData(body));
    }

    public ProductResponse postProducts(ProductRequest body) {
        return deserialize(postProductsResponse(body), ProductResponse.class);
    }

    @Step("PUT " + PRODUCTS + "/{id}")
    public APIResponse updateProductsResponse(String id, ProductRequest body) {
        return request.put(baseUri + PRODUCTS + "/" + id, RequestOptions.create()
                .setHeader("Authorization", auth("PUT", PRODUCTS + "/" + id))
                .setData(body));
    }

    public ProductResponse updateProduct(String id, ProductRequest body) {
        return deserialize(updateProductsResponse(id, body), ProductResponse.class);
    }

    @Step("DELETE " + PRODUCTS + "/{id}")
    public APIResponse deleteProductsResponse(String id) {
        return request.delete(baseUri + PRODUCTS + "/" + id, RequestOptions.create()
                .setHeader("Authorization", auth("DELETE", PRODUCTS + "/" + id)));
    }

    private <T> T deserialize(APIResponse response, Class<T> clazz) {
        try {
            return mapper.readValue(response.body(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize response to " + clazz.getSimpleName(), e);
        }
    }

    private <T> List<T> deserializeList(APIResponse response, Class<T> clazz) {
        try {
            return mapper.readValue(response.body(),
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize response to List<" + clazz.getSimpleName() + ">", e);
        }
    }
}

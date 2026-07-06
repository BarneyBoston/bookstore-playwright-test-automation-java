package app.bookstore.api;

import app.bookstore.api.coupon.CouponResponse;
import app.bookstore.api.coupon.PostCouponRequest;
import app.bookstore.api.product.ProductRequest;
import app.bookstore.api.product.ProductResponse;
import app.bookstore.helpers.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Step;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.util.List;

public class BookStoreApiController {

    private final APIRequestContext request;
    private final ObjectMapper mapper;
    private final String consumerKey;
    private final String consumerSecret;
    private final String baseUri;

    private static final String PRODUCTS = "/products";
    private static final String COUPONS = "/coupons";
    private static final String CUSTOMERS = "/customers";
    private static final String ORDERS = "/orders";
    private static final String REVIEWS = "/products/reviews";
    private static final Logger log = LoggerFactory.getLogger(BookStoreApiController.class);

    public BookStoreApiController(APIRequestContext request) {
        Config config = Config.getInstance();
        this.request = request;
        this.consumerKey = config.getConsumerKey();
        this.consumerSecret = config.getConsumerSecret();
        this.baseUri = config.getBaseUri();
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    private String getAuthHeader(String method, String url) throws OAuthException {
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
        return executeRequest("GET", baseUri + PRODUCTS,
                RequestOptions.create().setHeader("Authorization", auth("GET", PRODUCTS)), null);
    }

    public List<ProductResponse> getProducts() {
        return deserializeList(getProductsResponse(), ProductResponse.class);
    }

    @Step("POST " + PRODUCTS)
    public APIResponse postProductsResponse(ProductRequest body) {
        String bodyJson = toJson(body);
        return executeRequest("POST", baseUri + PRODUCTS,
                RequestOptions.create()
                        .setHeader("Authorization", auth("POST", PRODUCTS))
                        .setData(bodyJson), bodyJson);
    }

    public ProductResponse postProducts(ProductRequest body) {
        return deserialize(postProductsResponse(body), ProductResponse.class);
    }

    @Step("PUT " + PRODUCTS + "/{id}")
    public APIResponse updateProductsResponse(String id, ProductRequest body) {
        String bodyJson = toJson(body);
        return executeRequest("PUT", baseUri + PRODUCTS + "/" + id,
                RequestOptions.create()
                        .setHeader("Authorization", auth("PUT", PRODUCTS + "/" + id))
                        .setData(bodyJson), bodyJson);
    }

    public ProductResponse updateProduct(String id, ProductRequest body) {
        return deserialize(updateProductsResponse(id, body), ProductResponse.class);
    }

    @Step("DELETE " + PRODUCTS + "/{id}")
    public APIResponse deleteProductsResponse(String id) {
        return executeRequest("DELETE", baseUri + PRODUCTS + "/" + id,
                RequestOptions.create().setHeader("Authorization", auth("DELETE", PRODUCTS + "/" + id)), null);
    }

    // ── COUPONS ──────────────────────────────────────────

    @Step("POST " + COUPONS)
    public APIResponse postCouponsResponse(PostCouponRequest body) {
        String bodyJson = toJson(body);
        return executeRequest("POST", baseUri + COUPONS,
                RequestOptions.create()
                        .setHeader("Authorization", auth("POST", PRODUCTS))
                        .setData(bodyJson), bodyJson);
    }

    public CouponResponse postCoupon(PostCouponRequest body) {
        return deserialize(postCouponsResponse(body), CouponResponse.class);
    }

    // ── HELPERS ──────────────────────────────────────────

    /**
     * Execute HTTP request via Playwright's APIRequestContext and log request/response
     * both to stdout and as Allure attachments so logs are available locally and in CI reports.
     *
     * @param method  HTTP method (GET/POST/PUT/DELETE)
     * @param url     full URL
     * @param options RequestOptions instance (can be null)
     * @param requestBody optional serialized request body (null if none)
     * @return APIResponse returned by Playwright
     */
    private APIResponse executeRequest(String method, String url, RequestOptions options, String requestBody) {
        String shortRequest = method + " " + url;
        try {
            // Log request
            StringBuilder reqLog = new StringBuilder();
            reqLog.append("REQUEST: ").append(shortRequest).append(System.lineSeparator());
            if (requestBody != null) {
                reqLog.append("Body:\n").append(requestBody).append(System.lineSeparator());
            }

            String reqContent = reqLog.toString();
                    Allure.addAttachment("API Request - " + shortRequest, reqContent);
                    if (log.isInfoEnabled()) {
                        log.info("API Request - {}\n{}", shortRequest, reqContent);
                    }

                    APIResponse response = switch (method.toUpperCase()) {
                        case "GET" -> request.get(url, options);
                        case "POST" -> request.post(url, options);
                        case "PUT" -> request.put(url, options);
                        case "DELETE" -> request.delete(url, options);
                        default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
                    };

            String responseBody = getResponseTextSafe(response);

            String respContent = "RESPONSE: " + response.status() + " " + response.statusText() + System.lineSeparator() +
                    "URL: " + url + System.lineSeparator() +
                    "Body:\n" + responseBody + System.lineSeparator();
            Allure.addAttachment("API Response - " + shortRequest + " (status=" + response.status() + ")", respContent);
            if (log.isInfoEnabled()) {
                log.info("API Response - {} (status={})\n{}", shortRequest, response.status(), respContent);
            }

            return response;
        } catch (RuntimeException re) {
            // Attach exception to Allure with context and rethrow with contextual information
            String err = "REQUEST FAILED: " + shortRequest + " -> " + re.getMessage();
            Allure.addAttachment("API Error - " + shortRequest, err + System.lineSeparator() + "Exception: " + re.getClass().getName() + ": " + re.getMessage());
            throw new RuntimeException("Failed to execute request: " + shortRequest, re);
        }
    }

    // Extracted helper to safely read response text and avoid duplicating try/catch
    private String getResponseTextSafe(APIResponse response) {
        try {
            return response.text();
        } catch (Exception e) {
            return "<unavailable: " + e.getMessage() + ">";
        }
    }

    private String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
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

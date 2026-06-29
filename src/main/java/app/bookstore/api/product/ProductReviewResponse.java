package app.bookstore.api.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductReviewResponse {
    private String id;
    private String productId;
    private String status;
}

package app.bookstore.db.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRecord {
    @JsonAlias("product_id")
    private int productId;
    @JsonAlias("min_price")
    private double minPrice;
    @JsonAlias("max_price")
    private double maxPrice;
}

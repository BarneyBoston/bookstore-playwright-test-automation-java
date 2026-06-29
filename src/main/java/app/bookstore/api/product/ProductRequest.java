package app.bookstore.api.product;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
    private String name;
    private String type;
    @JsonAlias("regular_price")
    private String regularPrice;
    private String description;
    @JsonAlias("short_description")
    private String shortDescription;
}

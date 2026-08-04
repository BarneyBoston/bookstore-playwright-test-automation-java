package app.bookstore.api.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersResponse {
    private Integer id;
    private Billing billing;
}

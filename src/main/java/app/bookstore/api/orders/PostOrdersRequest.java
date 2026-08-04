package app.bookstore.api.orders;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PostOrdersRequest {
    @JsonAlias("payment_method")
    private String paymentMethod;
    @JsonAlias("payment_method_title")
    private String paymentMethodTitle;
    @JsonAlias("set_paid")
    private String setPaid;
    private Billing billing;
}

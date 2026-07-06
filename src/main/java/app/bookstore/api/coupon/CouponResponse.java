package app.bookstore.api.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponResponse {
    private String id;
    private String code;
    private String amount;
    private String status;
}

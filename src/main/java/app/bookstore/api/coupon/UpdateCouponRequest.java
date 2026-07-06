package app.bookstore.api.coupon;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCouponRequest {
    private String amount;
}

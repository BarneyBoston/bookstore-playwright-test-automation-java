package app.bookstore.api.coupon;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCouponRequest {
    private String code;
    private String discount_type;
    private String amount;
    private boolean individual_use;
    private boolean exclude_sale_items;
    private String minimum_amount;
}

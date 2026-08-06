package app.bookstore.api.coupon;

import app.bookstore.api.BaseApiTest;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Random;

@Epic("Coupon Controller Response Tests")
public class CouponControllerResponseTests extends BaseApiTest {

    @Test(priority = 1, description = "Post a new coupon and verify response status code is 201")
    public void postCouponResponseTest() {
        var request = PostCouponRequest.builder()
                .code(String.valueOf(Math.abs(new Random().nextInt())))
                .discount_type("percent")
                .amount(String.valueOf(Math.abs(new Random().nextInt())))
                .minimum_amount("1")
                .build();
        var response = controller().postCouponsResponse(request);

        Assertions.assertThat(response.status()).isEqualTo(201);
    }

    @Test(priority = 2, description = "Get coupons and verify response status code is 200")
    public void getCouponResponseTest() {
        var response = controller().getCouponsResponse();

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @Test(priority = 3, description = "Update coupon and verify response status code is 200")
    public void updateCouponResponseTest() {
        var couponId = controller().getCoupons().stream().map(CouponResponse::getId).toList().getFirst();

        var request = UpdateCouponRequest.builder()
                .amount("5")
                .build();

        var response = controller().updateCouponsResponse(couponId, request);

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @Test(priority = 4, description = "Delete coupon and verify response status code is 200")
    public void deleteCouponResponseTest() {
        var couponId = controller().getCoupons().stream().map(CouponResponse::getId).toList().getFirst();

        var response = controller().deleteCouponsResponse(couponId);

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @AfterClass
    public void localCleanUp() {
        try {
            var couponIds = controller().getCoupons().stream().map(CouponResponse::getId).toList();

            if (!couponIds.isEmpty()) {
                couponIds.forEach(coupon -> controller().deleteCouponsResponse(coupon));
            }
        } catch (Exception _) {
            log.info("Skipping due to lack of orderIds to clean");
        }
    }
}

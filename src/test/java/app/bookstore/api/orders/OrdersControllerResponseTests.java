package app.bookstore.api.orders;

import app.bookstore.api.BaseApiTest;
import io.qameta.allure.Epic;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@Epic("Orders Controller Response Tests")
public class OrdersControllerResponseTests extends BaseApiTest {

    @Test(priority = 1, description = "Verify that POST /orders returns status code 201")
    public void postOrdersResponseTest() {
        var request = PostOrdersRequest.builder()
                .build();
        var response = controller().postOrdersResponse(request);

        Assertions.assertThat(response.status())
                .isEqualTo(201);
    }

    @Test(priority = 2, description = "Verify that GET /orders returns status code 200")
    public void getOrdersResponseTest() {
        var response = controller().getOrdersResponse();

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @Test(priority = 3, description = "Verify that PUT /orders/{id} returns status code 200")
    public void updateOrdersResponseTest() {
        var id = controller().getOrders().stream().map(OrdersResponse::getId).toList().getFirst();

        var request = PutOrdersRequest.builder()
                .build();
        var response = controller().updateOrdersResponse(id.toString(), request);

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @Test(priority = 4, description = "Verify that DELETE /orders/{id} returns status code 200")
    public void deleteOrdersResponseTest() {
        var id = controller().getOrders().stream().map(OrdersResponse::getId).toList().getFirst();

        var response = controller().deleteOrdersResponse(id.toString());

        Assertions.assertThat(response.status())
                .isEqualTo(200);
    }

    @AfterClass
    public void localCleanUp() {
        try {
            var orderIds = controller().getOrders().stream().map(OrdersResponse::getId).toList();

            if (!orderIds.isEmpty()) {
                orderIds.forEach(order -> controller().deleteOrdersResponse(order.toString()));
            }
        } catch (Exception _) {
            log.info("Skipping due to lack of orderIds to clean");
        }
    }
}

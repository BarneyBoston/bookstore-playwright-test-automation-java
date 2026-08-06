package app.bookstore.api.customers;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCustomersRequest {
    @JsonAlias("first_name")
    private String firstName;
}

package app.bookstore.api.orders;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Billing {
    @JsonAlias("first_name")
    private String firstName;
    @JsonAlias("last_name")
    private String lastName;
    private String company;
    @JsonAlias("address_1")
    private String address1;
    @JsonAlias("address_2")
    private String address2;
    private String city;
    private String state;
    private String postcode;
    private String country;
    private String email;
    private String phone;

    @JsonCreator
    @SuppressWarnings("unused")
    public static Billing create(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("company") String company,
            @JsonProperty("address_1") String address1,
            @JsonProperty("address_2") String address2,
            @JsonProperty("city") String city,
            @JsonProperty("state") String state,
            @JsonProperty("postcode") String postcode,
            @JsonProperty("country") String country,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone) {
        return Billing.builder()
                .firstName(firstName)
                .lastName(lastName)
                .company(company)
                .address1(address1)
                .address2(address2)
                .city(city)
                .state(state)
                .postcode(postcode)
                .country(country)
                .email(email)
                .phone(phone)
                .build();
    }
}

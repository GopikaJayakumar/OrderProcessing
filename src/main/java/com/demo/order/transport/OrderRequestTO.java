package com.demo.order.transport;

import com.demo.order.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestTO {
    @JsonProperty(value="item_id")
    private String itemId;
    @JsonProperty(value="item_title")
    private String itemTitle;
    @JsonProperty(value="purchased_quantity")
    private int purchasedQuantity;
    @JsonProperty(value="price")
    private String price;
    @JsonProperty(value="first_name")
    private String firstName;
    @JsonProperty(value="last_name")
    private String lastName;
    @JsonProperty(value="phone_number")
    private String phoneNumber;
    @JsonProperty(value="shipping_address")
    private String shippingAddress;
    @JsonProperty(value="billing_address")
    private String billingAddress;
    @JsonProperty(value="retailer_name")
    private String retailerName;
}

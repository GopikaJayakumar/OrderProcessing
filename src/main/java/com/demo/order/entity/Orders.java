package com.demo.order.entity;

import com.demo.order.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @PrimaryKey(value = "order_id")
    private UUID orderId;
    @Column(value="customer_id")
    private String customerId;
    @Column(value="item_id")
    private String itemId;
    @Column(value="item_title")
    private String itemTitle;
    @Column(value="transaction_date")
    private LocalDate transactionDate;
    @Column(value="purchased_quantity")
    private int purchasedQuantity;
    @Column(value="price")
    private String price;
    @Column(value="first_name")
    private String firstName;
    @Column(value="last_name")
    private String lastName;
    @Column(value="phone_number")
    private String phoneNumber;
    @Column(value="shipping_address")
    private String shippingAddress;
    @Column(value="billing_address")
    private String billingAddress;
    @Column(value="retailer_name")
    private String retailerName;
    @Column(value="expected_delivery")
    private LocalDate expectedDelivery;
    @Column(value="order_status")
    private OrderStatus orderStatus;
}

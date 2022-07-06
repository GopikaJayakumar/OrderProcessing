package com.demo.order.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.demo.order.entity.Orders;
import com.demo.order.exception.InvalidInputException;
import com.demo.order.exception.ResourceUnAvailableException;
import com.demo.order.repository.OrderRepository;
import com.demo.order.transport.OrderRequestTO;
import com.demo.order.util.OrderStatus;
import com.demo.order.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private final KafkaTemplate<String, Orders> kafkaTemplate;

    @Autowired
    OrderRepository orderRepository;

    public Orders createNewOrder(
            String guestId,
            OrderRequestTO orderRequestTO
    ) throws InvalidInputException {
        ValidationUtils.validate(orderRequestTO);
        Orders order = Orders.builder()
                .orderId(Uuids.timeBased())
                .customerId(guestId)
                .itemId(orderRequestTO.getItemId())
                .itemTitle(orderRequestTO.getItemTitle())
                .transactionDate(LocalDate.now())
                .purchasedQuantity(orderRequestTO.getPurchasedQuantity())
                .price(orderRequestTO.getPrice())
                .firstName(orderRequestTO.getFirstName())
                .lastName(orderRequestTO.getLastName())
                .phoneNumber(orderRequestTO.getPhoneNumber())
                .shippingAddress(orderRequestTO.getShippingAddress())
                .billingAddress(orderRequestTO.getBillingAddress())
                .retailerName(orderRequestTO.getRetailerName())
                .expectedDelivery(LocalDate.now().plusDays(2))
                .orderStatus(OrderStatus.PLACED)
                .build();

        Message<Orders> produceOrder = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, "order-details")
                .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .setHeader("event_type", "order-processing")
                .setHeader("uuid", UUID.randomUUID().toString())
                .setHeader("correlation_id", "order-producer")
                .setHeader("source", "order-processing-app")
                .build();

        orderRepository.save(order);
        ListenableFuture<SendResult<String, Orders>> future =
                kafkaTemplate.send(produceOrder);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Orders>>() {
            @Override
            public void onSuccess(SendResult<String, Orders> result) {
                logger.info("Message [{}] delivered with offset {}",
                        produceOrder,
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.warn("Unable to deliver message [{}]. {}",
                        produceOrder,
                        ex.getMessage());
            }
        });
        return order;

    }

    public Orders getAllOrders(UUID orderId) {
        Optional<Orders> order = orderRepository.findById(orderId);
        if(order.isPresent())
            return order.get();
        else
            throw new ResourceUnAvailableException();
    }

    public String getOrderStatus(UUID orderId) throws ResourceUnAvailableException{
        Optional<Orders> order = orderRepository.findById(orderId);
        if(order.isPresent())
            return order.get().getOrderStatus().toString();
        else
            throw new ResourceUnAvailableException();
    }
}

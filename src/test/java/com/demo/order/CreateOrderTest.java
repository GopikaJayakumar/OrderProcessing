package com.demo.order;

import com.demo.order.entity.Orders;
import com.demo.order.exception.InvalidInputException;
import com.demo.order.repository.OrderRepository;
import com.demo.order.service.OrderService;
import com.demo.order.transport.OrderRequestTO;
import com.demo.order.util.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class CreateOrderTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private KafkaTemplate<Orders, UUID> kafkaTemplate;
    @Test
    public void createOrder_test_happy_path() throws Exception {
        OrderRequestTO orderRequestTO = new OrderRequestTO("item_id", "item_title", 1, "25", "test", "test", "1234567890", "address", "address", "retailer");
        Orders orders = new Orders(UUID.randomUUID(), "1234", "item_id", "item_title", LocalDate.now(), 1, "25", "test", "test", "1234567890", "address", "address", "retailer", LocalDate.now().plusDays(2), OrderStatus.PLACED);
        Orders order = orderService.createNewOrder("123", orderRequestTO);
        Message<Orders> message = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, "order-details")
                .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .setHeader("event_type", "order-processing")
                .setHeader("uuid", UUID.randomUUID().toString())
                .setHeader("correlation_id", "order-producer")
                .setHeader("source", "order-processing-app")
                .build();
        ListenableFuture<SendResult<Orders, UUID>> future = new SettableListenableFuture<>();
        Mockito.when(orderRepository.save(order)).thenReturn(orders);
        Mockito.when(kafkaTemplate.send(message)).thenReturn(future);
        Assertions.assertEquals(order.getOrderStatus().toString(), "PLACED");
    }

    @Test
    public void create_order_when_phone_number_is_not_valid() throws InvalidInputException {
        OrderRequestTO orderRequestTO = new OrderRequestTO("item_id", "item_title", 1, "25", "test", "test", "123456789", "address", "address", "retailer");
        Assertions.assertThrows(InvalidInputException.class, () -> {
            orderService.createNewOrder("123", orderRequestTO);
        });
    }

    @Test
    public void create_order_when_item_info_is_empty() throws InvalidInputException {
        OrderRequestTO orderRequestTO = new OrderRequestTO("item_id", null, 1, "25", "test", "test", "1234567890", "address", "address", "retailer");
        Assertions.assertThrows(InvalidInputException.class, () -> {
            orderService.createNewOrder("123", orderRequestTO);
        });
    }
}

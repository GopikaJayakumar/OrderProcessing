package com.demo.order.consumer;

import com.demo.order.entity.Orders;
import com.demo.order.repository.OrderRepository;
import com.demo.order.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    OrderRepository orderRepository;

        @KafkaListener(topics = "order-details",
                groupId = "group_id")

        public void consume(Orders order)
        {
            if(orderRepository.findById(order.getOrderId()).isPresent()) {
                order.setOrderStatus(OrderStatus.PROCESSED);
                orderRepository.insert(order);
            }
            else {
                logger.error("Not found");
            }

        }
}

package com.demo.order.controller;

import com.demo.order.entity.Orders;
import com.demo.order.exception.InvalidInputException;
import com.demo.order.exception.ResourceUnAvailableException;
import com.demo.order.service.OrderService;
import com.demo.order.transport.OrderRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * POST method for creating an order which in turn stores in the orders table of cassandra db and produce the message to order-details topic
     * @param customerId
     * body - OrderRequestTO
     * @return the order details as saved in the db
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    ResponseEntity<Orders> createOrder(@RequestHeader String customerId, @RequestBody OrderRequestTO orderRequest) throws InvalidInputException {
        return new ResponseEntity<>(orderService.createNewOrder(customerId, orderRequest), HttpStatus.CREATED);
    }

    /**
     *  GET method to view the order response corresponding to an orderId returned from the POST response
     * @param orderId
     * @return order details as saved in the db
     */
    @RequestMapping(value = "/{order_id}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Orders> getOrders(@PathVariable(value ="order_id") UUID orderId) throws ResourceUnAvailableException {
        return new ResponseEntity<>(orderService.getAllOrders(orderId), HttpStatus.OK);
    }

    /**
     * GET method to return the order status based on order id
     * @param orderId
     * @return String order status (PLACED, PROCESSED)
     */
    @RequestMapping(value = "/getStatus/{order_id}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<String> getOrderStatus(@PathVariable(value ="order_id") UUID orderId) throws ResourceUnAvailableException {
        return new ResponseEntity<>(orderService.getOrderStatus(orderId), HttpStatus.OK);
    }

}

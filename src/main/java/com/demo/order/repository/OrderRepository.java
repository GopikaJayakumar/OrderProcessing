package com.demo.order.repository;

import com.demo.order.entity.Orders;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface OrderRepository extends CassandraRepository<Orders, UUID> {
}

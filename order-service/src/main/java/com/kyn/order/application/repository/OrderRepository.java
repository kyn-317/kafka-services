package com.kyn.order.application.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.order.application.entity.Order;
import com.kyn.order.common.enums.OrderStatus;

import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {
    
    Mono<Order> findByOrderIdAndStatus(UUID orderId, OrderStatus status);
}

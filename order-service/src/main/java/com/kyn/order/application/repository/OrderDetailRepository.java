package com.kyn.order.application.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.order.application.entity.OrderDetail;

import reactor.core.publisher.Flux;

@Repository
public interface OrderDetailRepository extends ReactiveCrudRepository<OrderDetail, UUID> {
    

    Flux<OrderDetail> findByOrderId(UUID orderId);
}

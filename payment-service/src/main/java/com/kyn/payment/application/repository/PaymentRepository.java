package com.kyn.payment.application.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.common.messages.payment.PaymentStatus;
import com.kyn.payment.application.entity.CustomerPayment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<CustomerPayment, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

    Mono<CustomerPayment> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);

    Flux<CustomerPayment> findByAccountId(UUID accountId);
}
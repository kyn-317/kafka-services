package com.kyn.payment.application.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.payment.application.entity.Customer;

import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, UUID> {

    Mono<Customer> findByEmail(String email);
}
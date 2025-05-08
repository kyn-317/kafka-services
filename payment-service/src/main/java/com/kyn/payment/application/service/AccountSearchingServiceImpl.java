package com.kyn.payment.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.payment.application.entity.Customer;
import com.kyn.payment.application.entity.CustomerPayment;
import com.kyn.payment.application.repository.CustomerRepository;
import com.kyn.payment.application.repository.PaymentRepository;
import com.kyn.payment.application.service.interfaces.AccountSearchingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountSearchingServiceImpl implements AccountSearchingService {

    private final CustomerRepository customerRepository;

    private final PaymentRepository paymentRepository;

    @Override
    public Flux<Customer> getAllCustomer() {

        return customerRepository.findAll();
    }

    @Override
    public Mono<Customer> getRandomCustomer() {
        return customerRepository.findAll()
                .collectList()
                .flatMap(customers -> Mono.just(customers.get((int)(Math.random() * customers.size()))));
    }

    @Override
    public Flux<CustomerPayment> getPaymentByCustomerId(UUID customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }
    
}

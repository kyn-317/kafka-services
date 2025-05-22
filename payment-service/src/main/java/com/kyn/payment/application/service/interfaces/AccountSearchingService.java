package com.kyn.payment.application.service.interfaces;

import java.util.UUID;

import com.kyn.payment.application.entity.Account;
import com.kyn.payment.application.entity.CustomerPayment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountSearchingService {
    
    Flux<Account> getAllAccount();

    Mono<Account> getRandomAccount();
    
    Flux<CustomerPayment> getPaymentByCustomerId(UUID customerId);
}

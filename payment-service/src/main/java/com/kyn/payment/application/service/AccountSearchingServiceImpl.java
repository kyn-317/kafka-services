package com.kyn.payment.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.payment.application.entity.Account;
import com.kyn.payment.application.entity.CustomerPayment;
import com.kyn.payment.application.repository.AccountRepository;
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

    private final AccountRepository accountRepository;

    private final PaymentRepository paymentRepository;

    @Override
    public Flux<Account> getAllAccount() {

        return accountRepository.findAll();
    }

    @Override
    public Mono<Account> getRandomAccount() {
        return accountRepository.findAll()
                .collectList()
                .flatMap(accounts -> Mono.just(accounts.get((int)(Math.random() * accounts.size()))));
    }

    @Override
    public Flux<CustomerPayment> getPaymentByCustomerId(UUID customerId) {
        return accountRepository.findByCustomerId(customerId)
        .flatMapMany(account -> paymentRepository.findByAccountId(account.getId()));
    }
    
}

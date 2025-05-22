package com.kyn.payment.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.payment.application.entity.Account;
import com.kyn.payment.application.entity.CustomerPayment;
import com.kyn.payment.application.repository.AccountRepository;
import com.kyn.payment.application.repository.PaymentRepository;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/db-test")
public class dbTestController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public Flux<Account> test(){
        return this.accountRepository.findAll();
    }

    @GetMapping("payments")
    public Flux<CustomerPayment> payments(){
        return this.paymentRepository.findAll();
    }
}

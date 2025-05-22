package com.kyn.payment.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.payment.PaymentStatus;
import com.kyn.common.util.DuplicateEventValidator;
import com.kyn.payment.application.entity.Account;
import com.kyn.payment.application.entity.CustomerPayment;
import com.kyn.payment.application.mapper.EntityDtoMapper;
import com.kyn.payment.application.repository.AccountRepository;
import com.kyn.payment.application.repository.PaymentRepository;
import com.kyn.payment.common.dto.CartPaymentDto;
import com.kyn.payment.common.dto.CartPaymentProcessRequest;
import com.kyn.payment.common.exception.CustomerNotFoundException;
import com.kyn.payment.common.exception.InsufficientBalanceException;
import com.kyn.payment.common.service.CartPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartPaymentServiceImpl implements CartPaymentService {
    private static final Mono<Account> ACCOUNT_NOT_FOUND = Mono.error(new CustomerNotFoundException());
    private static final Mono<Account> INSUFFICIENT_BALANCE = Mono.error(new InsufficientBalanceException());
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    
    @Override
    public Mono<CartPaymentDto> process(CartPaymentProcessRequest request) {
        return DuplicateEventValidator.validate(
            this.paymentRepository.existsByOrderId(request.orderId()),
            this.accountRepository.findByCustomerId(request.customerId())
        ).
        switchIfEmpty(ACCOUNT_NOT_FOUND)
        .filter(c -> c.getBalance() >= request.amount())
        .switchIfEmpty(INSUFFICIENT_BALANCE)
        .flatMap(c -> this.deductPayment(c,request))
        .doOnNext(dto -> log.info("payment processed for {}", dto.orderId()));
    }    
    private Mono<CartPaymentDto> deductPayment(Account account, CartPaymentProcessRequest request) {
        var customerPayment = EntityDtoMapper.toCustomerPayment(request, account.getId());
        account.setBalance(account.getBalance() - request.amount());
        customerPayment.setStatus(PaymentStatus.PROCESSED);
        return this.accountRepository.save(account)
                                      .then(this.paymentRepository.save(customerPayment))
                                      .map(EntityDtoMapper::toDto);
    }

    @Override
    public Mono<CartPaymentDto> refund(UUID orderId) {    
        return this.paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.PROCESSED)
            .zipWhen(cp -> this.accountRepository.findById(cp.getAccountId()))
            .flatMap(t -> this.refundPayment(t.getT1(), t.getT2()))
            .doOnNext(dto -> log.info("refunded amount {} for {}", dto.amount(), dto.orderId()));

    }

    private Mono<CartPaymentDto> refundPayment(CustomerPayment customerPayment, Account account) {
        account.setBalance(account.getBalance() + customerPayment.getAmount());
        customerPayment.setStatus(PaymentStatus.REFUNDED);
        return this.accountRepository.save(account)
                                      .then(this.paymentRepository.save(customerPayment))
                                      .map(EntityDtoMapper::toDto);
    }
    
    
}

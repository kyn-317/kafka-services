package com.kyn.payment.common.service;

import java.util.UUID;

import com.kyn.payment.common.dto.CartPaymentDto;


import reactor.core.publisher.Mono;

public interface CartPaymentService {
    
    Mono<CartPaymentDto> process(PaymentProcessRequest request);

    Mono<CartPaymentDto> refund(UUID orderId);
}

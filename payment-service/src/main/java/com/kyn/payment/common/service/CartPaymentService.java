package com.kyn.payment.common.service;

import java.util.UUID;

import com.kyn.payment.common.dto.CartPaymentDto;
import com.kyn.payment.common.dto.CartPaymentProcessRequest;

import reactor.core.publisher.Mono;

public interface CartPaymentService {
    
    Mono<CartPaymentDto> process(CartPaymentProcessRequest request);

    Mono<CartPaymentDto> refund(UUID orderId);
}

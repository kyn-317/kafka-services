package com.kyn.payment.common.service;

import java.util.UUID;

import com.kyn.payment.common.dto.PaymentDto;
import com.kyn.payment.common.dto.PaymentProcessRequest;

import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<PaymentDto> process(PaymentProcessRequest request);

    Mono<PaymentDto> refund(UUID orderId);

}
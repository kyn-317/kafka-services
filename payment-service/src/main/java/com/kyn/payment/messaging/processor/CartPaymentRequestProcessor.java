package com.kyn.payment.messaging.processor;

import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.processor.CartRequestProcessor;


import reactor.core.publisher.Mono;

public interface CartPaymentRequestProcessor extends CartRequestProcessor<CartPaymentRequest, CartPaymentResponse> {

    @Override
    default Mono<CartPaymentResponse> process(CartPaymentRequest request) {
        return switch (request){
            case CartPaymentRequest.Process p -> this.handle(p);
            case CartPaymentRequest.Refund p -> this.handle(p);
        };
    }

    Mono<CartPaymentResponse> handle(CartPaymentRequest.Process request);

    Mono<CartPaymentResponse> handle(CartPaymentRequest.Refund request);

}
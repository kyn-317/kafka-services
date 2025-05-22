package com.kyn.payment.messaging.mapper;

import java.util.function.Function;

import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.payment.common.dto.CartPaymentDto;
import com.kyn.payment.common.dto.CartPaymentProcessRequest;

import reactor.core.publisher.Mono;

public class CartMapper {
    public static CartPaymentProcessRequest toProcessRequest(CartPaymentRequest.Process request) {
        return CartPaymentProcessRequest.builder()
                                    .orderId(request.requestItem().getOrderId())
                                    .amount(request.requestItem().getTotalPrice())
                                    .customerId(request.requestItem().getCustomerId())
                                    .build();
    }

    public static CartPaymentResponse toProcessedResponse(CartPaymentRequest.Process request, CartPaymentDto dto) {
        return CartPaymentResponse.Processed.builder()
                                        .responseItem(request.requestItem())
                                        .paymentId(dto.paymentId())
                                        .build();
    }

    public static Function<Throwable, Mono<CartPaymentResponse>> toPaymentDeclinedResponse(CartPaymentRequest.Process request) {
        return ex -> Mono.fromSupplier(() -> CartPaymentResponse.Declined.builder()
                                                                     .responseItem(request.requestItem())
                                                                     .message(ex.getMessage())
                                                                     .build());
    }

}

package com.kyn.payment.messaging.mapper;

import java.util.function.Function;

import com.kyn.common.messages.payment.PaymentRequest;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.payment.common.dto.PaymentDto;
import com.kyn.payment.common.dto.PaymentProcessRequest;

import reactor.core.publisher.Mono;

public class MessageDtoMapper {

    public static PaymentProcessRequest toProcessRequest(PaymentRequest.Process request) {
        return PaymentProcessRequest.builder()
                                    .orderId(request.orderId())
                                    .amount(request.amount())
                                    .customerId(request.customerId())
                                    .build();
    }

    public static PaymentResponse toProcessedResponse(PaymentDto dto) {
        return PaymentResponse.Processed.builder()
                                        .orderId(dto.orderId())
                                        .paymentId(dto.paymentId())
                                        .customerId(dto.customerId())
                                        .amount(dto.amount())
                                        .build();
    }

    public static Function<Throwable, Mono<PaymentResponse>> toPaymentDeclinedResponse(PaymentRequest.Process request) {
        return ex -> Mono.fromSupplier(() -> PaymentResponse.Declined.builder()
                                                                     .orderId(request.orderId())
                                                                     .message(ex.getMessage())
                                                                     .build());
    }

}
package com.kyn.payment.common.dto;

import java.util.UUID;

import com.kyn.common.messages.payment.PaymentStatus;

import lombok.Builder;

@Builder
public record PaymentDto(UUID paymentId,
                         UUID orderId,
                         UUID customerId,
                         Integer amount,
                         PaymentStatus status) {
}
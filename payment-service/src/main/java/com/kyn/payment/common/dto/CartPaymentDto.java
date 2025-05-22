package com.kyn.payment.common.dto;

import java.util.UUID;

import com.kyn.common.messages.payment.PaymentStatus;

import lombok.Builder;

@Builder
public record CartPaymentDto(UUID paymentId,
                         UUID orderId,
                         UUID accountId,
                         Double amount,
                         PaymentStatus status) {
}
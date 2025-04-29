package com.kyn.payment.common.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record PaymentProcessRequest(UUID customerId,
                                    UUID orderId,
                                    Integer amount) {
}
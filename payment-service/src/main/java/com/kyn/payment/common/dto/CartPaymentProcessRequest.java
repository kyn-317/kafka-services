package com.kyn.payment.common.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CartPaymentProcessRequest(UUID customerId,
                                    UUID orderId,
                                    Double amount) {
}
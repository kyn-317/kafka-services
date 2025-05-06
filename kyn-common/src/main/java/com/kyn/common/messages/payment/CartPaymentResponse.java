package com.kyn.common.messages.payment;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartResponse;

import lombok.Builder;

public sealed interface CartPaymentResponse extends CartResponse {
    
    @Builder
    record Processed(OrderSummaryDto orderSummary,
                    UUID paymentId) implements CartPaymentResponse {
    }

    @Builder
    record Declined(OrderSummaryDto orderSummary,
                    String message) implements CartPaymentResponse {
    }
}

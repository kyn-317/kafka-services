package com.kyn.common.messages.payment;

import java.util.UUID;

import org.springframework.core.annotation.Order;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;

import lombok.Builder;

public sealed interface CartPaymentRequest extends CartRequest {
    

    @Builder
    record Process(OrderSummaryDto orderSummary) implements CartPaymentRequest {
    }

    @Builder
    record Refund(OrderSummaryDto orderSummary) implements CartPaymentRequest {
    }

}

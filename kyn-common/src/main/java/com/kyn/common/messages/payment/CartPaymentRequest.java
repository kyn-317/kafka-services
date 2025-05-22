package com.kyn.common.messages.payment;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;

import lombok.Builder;

public sealed interface CartPaymentRequest extends CartRequest {
    

    @Builder
    record Process(OrderSummaryDto requestItem) implements CartPaymentRequest {
    }

    @Builder
    record Refund(OrderSummaryDto requestItem) implements CartPaymentRequest {
    }

}

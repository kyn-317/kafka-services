package com.kyn.common.messages.payment;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.Request;

import lombok.Builder;

public sealed interface PaymentRequest extends Request {

    @Builder
    record Process(UUID orderId,
                   UUID customerId,
                   Integer amount) implements PaymentRequest {
    }

    @Builder
    record Refund(UUID orderId) implements PaymentRequest {
    }

    @Builder
    record CartProcess(UUID orderId,
                UUID customerId,
                OrderSummaryDto orderSummary) implements PaymentRequest {
    }

}

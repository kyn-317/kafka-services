package com.kyn.common.messages.payment;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.Response;

import lombok.Builder;

public sealed interface PaymentResponse extends Response {

    @Builder
    record Processed(UUID orderId,
                     UUID paymentId,
                     UUID customerId,
                     Integer amount) implements PaymentResponse {

    }
    
    @Builder
    record Declined(UUID orderId,
                    String message) implements PaymentResponse {

    }

}

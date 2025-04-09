package com.kyn.common.messages.payment;

import com.kyn.common.messages.Response;
import lombok.Builder;

import java.util.UUID;

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

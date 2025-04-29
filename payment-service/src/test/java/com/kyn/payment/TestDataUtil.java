package com.kyn.payment;

import java.util.UUID;

import com.kyn.common.messages.payment.PaymentRequest;

public class TestDataUtil {

    public static PaymentRequest createProcessRequest(UUID orderId, UUID customerId, int amount) {
        return PaymentRequest.Process.builder()
                                      .orderId(orderId)
                                      .customerId(customerId)
                                      .amount(amount)
                                      .build();
    }

    public static PaymentRequest createRefundRequest(UUID orderId) {
        return PaymentRequest.Refund.builder()
                                     .orderId(orderId)
                                     .build();
    }

}
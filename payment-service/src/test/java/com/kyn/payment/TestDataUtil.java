package com.kyn.payment;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.payment.CartPaymentRequest;

public class TestDataUtil {

    public static CartPaymentRequest createProcessRequest(UUID orderId, UUID customerId, Double amount) {
        var dto = OrderSummaryDto.builder().orderId(orderId).customerId(customerId).totalPrice(amount).build();
        return CartPaymentRequest.Process.builder()
                                      .requestItem(dto)
                                      .build();
    }

    public static CartPaymentRequest createRefundRequest(UUID orderId) {
        var dto = OrderSummaryDto.builder().orderId(orderId).build();
        return CartPaymentRequest.Refund.builder()
                                     .requestItem(dto)
                                     .build();
    }

}
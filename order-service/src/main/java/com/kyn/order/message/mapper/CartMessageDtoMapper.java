package com.kyn.order.message.mapper;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.order.common.dto.OrderDto;

public class CartMessageDtoMapper {
/*     public static CartPaymentRequest toPaymentProcessRequest(PurchaseOrderDto dto) {
        return CartPaymentRequest.Process.builder()
                                     .orderId(dto.orderId())
                                     .amount(dto.amount())
                                     .customerId(dto.customerId())
                                     .build();
    }

    public static PaymentRequest toPaymentRefundRequest(UUID orderId) {
        return PaymentRequest.Refund.builder()
                                    .orderId(orderId)
                                    .build();
    }
    */

    public static CartInventoryRequest toInventoryDeductRequest(OrderSummaryDto dto) {
        return CartInventoryRequest.Deduct.builder()
                                      .requestItem(dto)
                                      .build();
    } 

    public static CartInventoryRequest toInventoryRestoreRequest(OrderSummaryDto dto) {
        return CartInventoryRequest.Restore.builder()
                                       .requestItem(dto)
                                       .build();
    }

}

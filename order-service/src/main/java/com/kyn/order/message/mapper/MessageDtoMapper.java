package com.kyn.order.message.mapper;

import com.kyn.common.messages.inventory.InventoryRequest;
import com.kyn.common.messages.payment.PaymentRequest;
import com.kyn.order.common.dto.PurchaseOrderDto;

import java.util.UUID;

public class MessageDtoMapper {

    public static PaymentRequest toPaymentProcessRequest(PurchaseOrderDto dto) {
        return PaymentRequest.Process.builder()
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

    public static InventoryRequest toInventoryDeductRequest(PurchaseOrderDto dto) {
        return InventoryRequest.Deduct.builder()
                                      .orderId(dto.orderId())
                                      .productId(dto.productId())
                                      .quantity(dto.quantity())
                                      .build();
    }

    public static InventoryRequest toInventoryRestoreRequest(UUID orderId) {
        return InventoryRequest.Restore.builder()
                                       .orderId(orderId)
                                       .build();
    }


}

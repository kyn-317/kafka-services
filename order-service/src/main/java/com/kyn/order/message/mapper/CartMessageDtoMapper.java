package com.kyn.order.message.mapper;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.payment.CartPaymentRequest;

public class CartMessageDtoMapper {
    public static CartPaymentRequest toPaymentProcessRequest(OrderSummaryDto dto) {
        return CartPaymentRequest.Process.builder()
                                     .requestItem(dto)
                                     .build();
    }

    public static CartPaymentRequest toPaymentRefundRequest(OrderSummaryDto dto) {
        return CartPaymentRequest.Refund.builder()
                                    .requestItem(dto)
                                    .build();
    }
   

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

package com.kyn.common.messages.inventory;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartResponse;

import lombok.Builder;

public sealed interface CartInventoryResponse extends CartResponse {
    
    @Builder
    record Deducted(OrderSummaryDto orderSummary
    ,UUID inventoryId) implements CartInventoryResponse {

    }

    @Builder
    record Declined(OrderSummaryDto orderSummary
    ,UUID InventoryId) implements CartInventoryResponse {

    }

}

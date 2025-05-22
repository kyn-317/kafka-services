package com.kyn.common.messages.inventory;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;

import lombok.Builder;

public sealed interface CartInventoryRequest extends CartRequest {
    @Builder
    record Deduct(OrderSummaryDto requestItem) implements CartInventoryRequest {

    }

    @Builder
    record Restore(OrderSummaryDto requestItem) implements CartInventoryRequest {

    }

}

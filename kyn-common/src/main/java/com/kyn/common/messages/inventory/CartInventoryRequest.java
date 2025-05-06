package com.kyn.common.messages.inventory;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;

import lombok.Builder;

public sealed interface CartInventoryRequest extends CartRequest {
    @Builder
    record Deduct(OrderSummaryDto orderSummary) implements CartInventoryRequest {

    }

    @Builder
    record Restore(OrderSummaryDto orderSummary) implements CartInventoryRequest {

    }

}

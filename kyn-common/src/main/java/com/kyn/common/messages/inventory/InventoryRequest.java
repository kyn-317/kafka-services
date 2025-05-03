package com.kyn.common.messages.inventory;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.Request;

import lombok.Builder;

public sealed interface InventoryRequest extends Request {

    @Builder
    record Deduct(UUID orderId,
                  UUID productId,
                  Integer quantity) implements InventoryRequest {

    }

    @Builder    
    record DeductCart(UUID orderId,
                      UUID customerId,
                      OrderSummaryDto orderSummary) implements InventoryRequest {

    }

    @Builder
    record Restore(UUID orderId) implements InventoryRequest {

    }

    @Builder
    record RestoreCart(UUID orderId,
                       UUID customerId,
                       OrderSummaryDto orderSummary) implements InventoryRequest {
    }
}

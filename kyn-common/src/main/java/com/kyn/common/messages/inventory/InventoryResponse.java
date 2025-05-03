package com.kyn.common.messages.inventory;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.Response;

import lombok.Builder;

public sealed interface InventoryResponse extends Response {

    @Builder
    record Deducted(UUID orderId,
                     UUID inventoryId,
                     UUID productId,
                     Integer quantity) implements InventoryResponse {

    }

    @Builder
    record DeductedCart(UUID orderId,
                        UUID customerId,
                        OrderSummaryDto orderSummary) implements InventoryResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements InventoryResponse {

    }
}

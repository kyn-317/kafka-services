package com.kyn.common.messages.inventory;

import com.kyn.common.messages.Response;
import lombok.Builder;

import java.util.UUID;

public sealed interface InventoryResponse extends Response {

    @Builder
    record Deducted(UUID orderId,
                     UUID inventoryId,
                     UUID productId,
                     Integer quantity) implements InventoryResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements InventoryResponse {

    }

}

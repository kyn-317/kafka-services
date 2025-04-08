package com.kyn.common.messages.inventory;

import com.kyn.common.messages.Request;
import lombok.Builder;

import java.util.UUID;

public sealed interface InventoryRequest extends Request {

    @Builder
    record Deduct(UUID orderId,
                  UUID productId,
                  Integer quantity) implements InventoryRequest {

    }

    @Builder
    record Restore(UUID orderId) implements InventoryRequest {

    }

}

package com.kyn.inventory.common.dto;


import lombok.Builder;

import java.util.UUID;

import com.kyn.common.messages.inventory.InventoryStatus;

@Builder
public record OrderInventoryDto(UUID inventoryId,
                                UUID orderId,
                                Integer productId,
                                Integer quantity,
                                InventoryStatus status) {
}

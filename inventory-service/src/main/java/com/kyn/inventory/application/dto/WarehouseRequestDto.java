package com.kyn.inventory.application.dto;

import java.util.UUID;

import com.kyn.inventory.application.enums.StorageRetrievalType;

import lombok.Builder;

@Builder
public record WarehouseRequestDto(
    UUID productId,
    UUID requesterId,
    UUID orderId,
    StorageRetrievalType retrievalType,
    Integer quantity
) {
    // Builder will be generated automatically for records
}

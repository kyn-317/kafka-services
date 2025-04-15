package com.kyn.inventory.application.dto;

import java.util.UUID;

import com.kyn.inventory.application.enums.StorageRetrievalType;

public record WarehouseRequestDto(
    UUID productId,
    UUID requesterId,
    UUID orderId,
    StorageRetrievalType retrievalType,
    Integer quantity
) {}

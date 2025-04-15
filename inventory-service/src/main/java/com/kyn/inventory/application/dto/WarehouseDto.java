package com.kyn.inventory.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kyn.inventory.application.enums.StorageRetrievalType;

public record WarehouseDto(
    UUID id,
    UUID productId,
    UUID requesterId,
    UUID orderId,
    StorageRetrievalType retrievalType,
    Integer quantity,
    String snapshotDate,
    LocalDateTime createdAt,
    String createdBy
) {}

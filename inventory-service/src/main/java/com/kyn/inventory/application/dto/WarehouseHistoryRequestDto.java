package com.kyn.inventory.application.dto;

import java.util.UUID;

import com.kyn.inventory.application.enums.StorageRetrievalType;

public record WarehouseHistoryRequestDto(
    String fromDate,
    String toDate,  
    UUID productId,
    StorageRetrievalType retrievalType
) {}

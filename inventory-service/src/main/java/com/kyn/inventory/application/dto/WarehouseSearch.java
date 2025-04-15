package com.kyn.inventory.application.dto;

import java.util.UUID;

import com.kyn.inventory.application.enums.StorageRetrievalType;

public record WarehouseSearch(
    UUID productId,
    String snapshotDate,
    StorageRetrievalType retrievalType,
    String fromDate,
    String toDate
) {}

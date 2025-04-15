package com.kyn.inventory.application.dto;

import java.util.UUID;

public record CurrentStock(
    UUID productId,
    Integer currentStock,
    String snapshotDate
) {}

package com.kyn.inventory.application.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.kyn.inventory.application.enums.StorageRetrievalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseHistory {

    @Id
    private UUID historyId;
    private UUID id;
    private UUID productId;
    private UUID requesterId;
    private UUID orderId;
    private StorageRetrievalType retrievalType;
    private Integer quantity;
    private String snapshotDate;
    private LocalDateTime createdAt;
    private String createdBy;
} 
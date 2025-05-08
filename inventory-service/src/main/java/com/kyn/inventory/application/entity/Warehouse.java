package com.kyn.inventory.application.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.kyn.inventory.application.enums.StorageRetrievalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("warehouse")
public class Warehouse {

    @Id
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

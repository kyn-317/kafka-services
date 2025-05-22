package com.kyn.inventory.application.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "warehouse_data", name = "warehouse")
public class Warehouse {

    @Id
    private UUID id;
    private UUID productId;
    private UUID requesterId;
    private UUID orderId;
    @Column("storage_retrieval_type")
    private String storageRetrievalType;
    private Integer quantity;
    private String snapshotDate;
    private LocalDateTime createdAt;
    private String createdBy;
}

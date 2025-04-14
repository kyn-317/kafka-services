package com.kyn.inventory.application.entity;

import java.util.UUID;

import com.kyn.inventory.application.enums.StorageRetrievalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    private UUID id;
    private UUID productId;
    private StorageRetrievalType retrievalType;
    private Integer quantity;
    private String snapshotDate;
    
}

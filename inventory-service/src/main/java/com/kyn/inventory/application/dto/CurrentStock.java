package com.kyn.inventory.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentStock {

    private UUID productId;
    private Integer quantity;
    private String snapshotDate;
    
}

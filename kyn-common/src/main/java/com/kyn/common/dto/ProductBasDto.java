package com.kyn.common.dto;

import java.time.LocalDateTime;

public record ProductBasDto(
    String _id,
    String productName,
    String productCategory,
    String productDescription,
    String productSpecification,
    Double productPrice,
    String productImage,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy
) {
    
}

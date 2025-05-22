package com.kyn.common.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    private UUID orderDetailId;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;
    private Double unitPrice;
    private Double amount;
}

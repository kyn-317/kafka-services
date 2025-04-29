package com.kyn.order.application.entity;


import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class OrderDetail extends BaseDocuments{
    @Id
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;
    private Double unitPrice;
    private Double amount;
    
}

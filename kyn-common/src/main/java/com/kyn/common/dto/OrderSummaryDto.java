package com.kyn.common.dto;

import java.util.List;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryDto {
    private UUID orderId;
    private UUID customerId;
    private Double totalPrice;
    private List<OrderDetailDto> orderDetails;
}

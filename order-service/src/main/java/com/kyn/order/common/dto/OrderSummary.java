package com.kyn.order.common.dto;

import java.util.List;
import java.util.UUID;

import com.kyn.order.application.entity.OrderDetail;
import com.kyn.order.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummary {
    
    private UUID orderId;
    private UUID customerId;
    private Double totalPrice;
    private OrderStatus status;
    private List<OrderDetail> orderDetails;
}

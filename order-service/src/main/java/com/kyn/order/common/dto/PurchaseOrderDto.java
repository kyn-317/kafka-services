package com.kyn.order.common.dto;

import com.kyn.order.common.enums.OrderStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PurchaseOrderDto(UUID orderId,
                               UUID customerId,
                               UUID productId,
                               Integer unitPrice,
                               Integer quantity,
                               Integer amount,
                               OrderStatus status,
                               Instant deliveryDate) {
}

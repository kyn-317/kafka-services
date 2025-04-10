package com.kyn.order.common.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderCreateRequest(UUID customerId,
                                 UUID productId,
                                 Integer quantity,
                                 Integer unitPrice) {
}

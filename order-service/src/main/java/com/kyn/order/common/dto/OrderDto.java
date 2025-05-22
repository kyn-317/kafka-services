package com.kyn.order.common.dto;

import java.util.UUID;

import com.kyn.order.common.enums.OrderStatus;

import lombok.Builder;

@Builder
public record OrderDto(UUID orderId,
                       UUID customerId,
                       Double totalPrice,
                       OrderStatus status) {}
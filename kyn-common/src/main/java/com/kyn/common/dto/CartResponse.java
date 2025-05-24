package com.kyn.common.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CartResponse(
    String _id,
    String email,
    List<CartItem> cartItems,
    Double totalPrice
) {}
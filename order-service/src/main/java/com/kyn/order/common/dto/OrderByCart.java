
package com.kyn.order.common.dto;

import java.util.UUID;

import com.kyn.common.dto.CartResponse;

import lombok.Builder;

@Builder
public record OrderByCart(UUID customerId, CartResponse cart) {
    
}

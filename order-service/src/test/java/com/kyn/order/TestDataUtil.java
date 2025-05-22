/* package com.kyn.order;

import java.util.UUID;

import com.kyn.order.common.dto.OrderCreateRequest;

public class TestDataUtil {

    public static OrderCreateRequest toRequest(UUID customerId, UUID productId, int unitPrice, int quantity) {
        return OrderCreateRequest.builder()
                                      .unitPrice(unitPrice)
                                      .quantity(quantity)
                                      .customerId(customerId)
                                      .productId(productId)
                                      .build();
    }

} */
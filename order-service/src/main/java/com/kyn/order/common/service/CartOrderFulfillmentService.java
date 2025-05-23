package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;


import reactor.core.publisher.Mono;

public interface CartOrderFulfillmentService {
    Mono<OrderSummaryDto> get(UUID orderId);

    Mono<OrderSummaryDto> complete(UUID orderId);

    Mono<OrderSummaryDto> cancel(UUID orderId);

}

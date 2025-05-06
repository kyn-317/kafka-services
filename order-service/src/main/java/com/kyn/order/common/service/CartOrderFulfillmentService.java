package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.common.dto.PurchaseOrderDto;

import reactor.core.publisher.Mono;

public interface CartOrderFulfillmentService {
    Mono<OrderSummaryDto> get(OrderSummaryDto orderSummary);

    Mono<OrderSummaryDto> complete(OrderSummaryDto orderSummary);

    Mono<OrderSummaryDto> cancel(OrderSummaryDto orderSummary);

}

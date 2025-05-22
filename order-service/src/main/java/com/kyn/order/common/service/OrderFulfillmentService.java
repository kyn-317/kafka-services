package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.order.common.dto.PurchaseOrderDto;

import reactor.core.publisher.Mono;

public interface OrderFulfillmentService {

    Mono<PurchaseOrderDto> get(UUID orderId);

    Mono<PurchaseOrderDto> complete(UUID orderId);

    Mono<PurchaseOrderDto> cancel(UUID orderId);

}

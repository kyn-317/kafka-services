package com.kyn.order.common.service;

import com.kyn.order.common.dto.OrderCreateRequest;
import com.kyn.order.common.dto.OrderDetails;
import com.kyn.order.common.dto.PurchaseOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {

    Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request);

    Flux<PurchaseOrderDto> getAllOrders();

    Mono<OrderDetails> getOrderDetails(UUID orderId);

}

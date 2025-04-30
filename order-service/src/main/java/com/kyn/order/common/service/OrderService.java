package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.dto.OrderCreateRequest;
import com.kyn.order.common.dto.OrderDetails;
import com.kyn.order.common.dto.OrderSummary;
import com.kyn.order.common.dto.PurchaseOrderDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request);

    Flux<PurchaseOrderDto> getAllOrders();

    Mono<OrderDetails> getOrderDetails(UUID orderId);

    Mono<OrderSummaryDto> placeOrderByCart(OrderByCart cart);
}

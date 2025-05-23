package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.common.dto.CartOrderDetails;
import com.kyn.order.common.dto.OrderByCart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartOrderService {

    

    Flux<OrderSummaryDto> getAllOrders();

    Mono<CartOrderDetails> getOrderDetails(UUID orderId);

    Mono<OrderSummaryDto> placeOrderByCart(OrderByCart cart);
}

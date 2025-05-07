package com.kyn.order.common.service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.common.dto.OrderByCart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {
    
    Mono<OrderSummaryDto> placeOrder(OrderByCart cart);

    Flux<OrderSummaryDto> getAllOrders();

    Mono<OrderSummaryDto> getOrderDetails(OrderSummaryDto orderSummary);
    

}

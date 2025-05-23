package com.kyn.order.application.service;

import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.application.entity.Order;
import com.kyn.order.application.mapper.CartMapper;
import com.kyn.order.application.repository.OrderDetailRepository;
import com.kyn.order.application.repository.OrderRepository;
import com.kyn.order.common.enums.OrderStatus;
import com.kyn.order.common.service.CartOrderFulfillmentService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartOrderFulfillmentServiceImpl implements CartOrderFulfillmentService {

    private final OrderRepository repository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Mono<OrderSummaryDto> get(UUID orderId) {
        return this.repository.findById(orderId)
        .flatMap(order -> this.orderDetailRepository.findByOrderId(orderId)
        .collectList()
        .map(details -> CartMapper.toOrderSummary(order, details)));
    }

    @Override
    public Mono<OrderSummaryDto> complete(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.COMPLETED));
    }

    @Override
    public Mono<OrderSummaryDto> cancel(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.CANCELLED));
    }

    private Mono<OrderSummaryDto> update(UUID orderId, Consumer<Order> consumer) {
        return this.repository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                              .doOnNext(consumer)
                              .flatMap(this.repository::save)
                              .flatMap(order -> this.orderDetailRepository.findByOrderId(orderId)
                              .collectList()
                              .map(details -> CartMapper.toOrderSummary(order, details)));
    }

   

}

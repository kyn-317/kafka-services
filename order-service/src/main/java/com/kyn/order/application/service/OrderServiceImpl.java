package com.kyn.order.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.order.application.entity.PurchaseOrder;
import com.kyn.order.application.mapper.EntityDtoMapper;
import com.kyn.order.application.repository.PurchaseOrderRepository;
import com.kyn.order.common.dto.OrderCreateRequest;
import com.kyn.order.common.dto.OrderDetails;
import com.kyn.order.common.dto.PurchaseOrderDto;
import com.kyn.order.common.enums.OrderStatus;
import com.kyn.order.common.service.OrderEventListener;
import com.kyn.order.common.service.OrderService;
import com.kyn.order.common.service.WorkflowActionRetriever;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionRetriever actionRetriever;

    @Override
    public Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request) {
        var purchaseOrder = PurchaseOrder.builder()
                              .orderId(UUID.randomUUID())
                              .customerId(request.customerId())
                              .productId(request.productId())
                              .quantity(request.quantity())
                              .unitPrice(request.unitPrice())
                              .amount(request.quantity() * request.unitPrice())
                              .status(OrderStatus.PENDING)
                              .build();
        
        return this.repository.save(purchaseOrder)
                      .map(EntityDtoMapper::toDto)
                      .doOnNext(eventListener::emitOrderCreated);
    }

    @Override
    public Flux<PurchaseOrderDto> getAllOrders() {
        return this.repository.findAll()
                      .map(EntityDtoMapper::toDto);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                      .map(EntityDtoMapper::toDto)
                      .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                      .map(t -> OrderDetails.builder()
                                  .order(t.getT1())
                                  .actions(t.getT2())
                                  .build());
    }
    
} 
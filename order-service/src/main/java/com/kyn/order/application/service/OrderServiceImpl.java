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
        var entity = EntityDtoMapper.toPurchaseOrder(request);
        return this.repository.save(entity)
                              .map(EntityDtoMapper::toPurchaseOrderDto)
                              .doOnNext(eventListener::emitOrderCreated);
    }

    @Override
    public Flux<PurchaseOrderDto> getAllOrders() {
        return this.repository.findAll()
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                              .map(EntityDtoMapper::toPurchaseOrderDto)
                              .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                              .map(t -> EntityDtoMapper.toOrderDetails(t.getT1(), t.getT2()));
    }

}

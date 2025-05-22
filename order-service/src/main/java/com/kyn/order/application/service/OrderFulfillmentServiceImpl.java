package com.kyn.order.application.service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.order.application.entity.PurchaseOrder;
import com.kyn.order.application.mapper.EntityDtoMapper;
import com.kyn.order.application.repository.PurchaseOrderRepository;
import com.kyn.order.common.dto.PurchaseOrderDto;
import com.kyn.order.common.enums.OrderStatus;
import com.kyn.order.common.service.OrderFulfillmentService;
import com.kyn.order.common.service.TemplateMessageEventListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final PurchaseOrderRepository repository;
    private final TemplateMessageEventListener messageProducer;

    @Override
    public Mono<PurchaseOrderDto> get(UUID orderId) {
        return this.repository.findById(orderId)
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }


    @Override
    public Mono<PurchaseOrderDto> complete(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.COMPLETED))
        .doOnSuccess( purchaseOrderDto -> 
        this.messageProducer.createMessage(
        TemplateMessageRequest.ORDER_COMPLETED.builder()
        .requestItem(Map.of("user_name", purchaseOrderDto.customerId().toString(), "order_details", purchaseOrderDto.toString()))
        .userId(purchaseOrderDto.customerId().toString())
        .orderId(orderId.toString())
        .build()));
    }

    @Override
    public Mono<PurchaseOrderDto> cancel(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.CANCELLED));
    }

    private Mono<PurchaseOrderDto> update(UUID orderId, Consumer<PurchaseOrder> consumer) {
        return this.repository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                              .doOnNext(consumer)
                              .flatMap(this.repository::save)
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }

}

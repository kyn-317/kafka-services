package com.kyn.order.application.service;

import com.kyn.common.util.DuplicateEventValidator;
import com.kyn.order.application.mapper.EntityDtoMapper;
import com.kyn.order.application.repository.OrderWorkflowActionRepository;
import com.kyn.order.common.dto.OrderWorkflowActionDto;
import com.kyn.order.common.enums.WorkflowAction;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import com.kyn.order.application.mapper.CartMapper;
import com.kyn.order.common.dto.CartOrderWorkflowActionDto;
import com.kyn.order.common.service.CartWorkflowActionRetriever;
import com.kyn.order.common.service.CartWorkflowActionTracker;

@Service
@RequiredArgsConstructor
public class CartWorkflowActionService implements CartWorkflowActionTracker, CartWorkflowActionRetriever {

    private final OrderWorkflowActionRepository repository;

    @Override
    public Flux<CartOrderWorkflowActionDto> retrieve(UUID orderId) {
        return this.repository.findByOrderIdOrderByCreatedAt(orderId)
                              .map(CartMapper::toCartOrderWorkflowActionDto);
    }

    @Override
    public Mono<Void> track(UUID orderId, WorkflowAction action) {
        return DuplicateEventValidator.validate(
                this.repository.existsByOrderIdAndAction(orderId, action),
                this.repository.save(EntityDtoMapper.toOrderWorkflowAction(orderId, action)) // defer if required
        ).then();
    }
}

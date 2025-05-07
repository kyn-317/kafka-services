package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.order.common.dto.CartOrderWorkflowActionDto;

import reactor.core.publisher.Flux;


public interface CartWorkflowActionRetriever {

    Flux<CartOrderWorkflowActionDto> retrieve(UUID orderId);

}

package com.kyn.order.common.service;

import com.kyn.order.common.dto.OrderWorkflowActionDto;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface WorkflowActionRetriever {

    Flux<OrderWorkflowActionDto> retrieve(UUID orderId);

}

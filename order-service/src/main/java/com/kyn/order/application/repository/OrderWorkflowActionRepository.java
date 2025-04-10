package com.kyn.order.application.repository;

import com.kyn.order.application.entity.OrderWorkFlowAction;
import com.kyn.order.common.enums.WorkflowAction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderWorkflowActionRepository extends ReactiveCrudRepository<OrderWorkFlowAction, UUID> {

    Mono<Boolean> existsByOrderIdAndAction(UUID orderId, WorkflowAction action);

    Flux<OrderWorkFlowAction> findByOrderIdOrderByCreatedAt(UUID orderId);

}

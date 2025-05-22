package com.kyn.order.common.service;

import java.util.UUID;

import com.kyn.order.common.enums.WorkflowAction;

import reactor.core.publisher.Mono;

public interface CartWorkflowActionTracker {

    Mono<Void> track(UUID orderId, WorkflowAction action);

}

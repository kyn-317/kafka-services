package com.kyn.order.common.service;

import com.kyn.order.common.enums.WorkflowAction;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface WorkflowActionTracker {

    Mono<Void> track(UUID orderId, WorkflowAction action);

}

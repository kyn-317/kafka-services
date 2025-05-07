package com.kyn.common.orchestrator;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.CartResponse;

public interface CartWorkflowOrchestrator {
    Publisher<CartRequest> orchestrate(CartResponse response);
}

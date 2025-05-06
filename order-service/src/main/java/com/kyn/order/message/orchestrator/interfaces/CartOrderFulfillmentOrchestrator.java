package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;


import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.CartResponse;
import com.kyn.common.messages.Request;
import com.kyn.common.messages.Response;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.orchestrator.CartWorkflowOrchestrator;
import com.kyn.common.orchestrator.WorkFlowOrchestrator;

import reactor.core.publisher.Mono;

public interface CartOrderFulfillmentOrchestrator extends CartWorkflowOrchestrator {

    Publisher<CartRequest> orderInitialRequests();
    @Override
    default Publisher<CartRequest> orchestrater(CartResponse response) {
        return switch (response) {
            case CartPaymentResponse r -> this.handle(r);
            case CartInventoryResponse r -> this.handle(r);
            default -> Mono.empty();
        };
    }

    Publisher<CartRequest> handle(CartPaymentResponse response);

    Publisher<CartRequest> handle(CartInventoryResponse response);

}

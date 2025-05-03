package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.Response;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.orchestrator.WorkFlowOrchestrator;

import reactor.core.publisher.Mono;

public interface CartOrderFulfillmentOrchestrator extends WorkFlowOrchestrator {

    Publisher<Request> orderInitialRequests();

    @Override
    default Publisher<Request> orchestrate(Response response) {
        return switch (response) {
            case PaymentResponse r -> this.handle(r);
            case InventoryResponse r -> this.handle(r);
            default -> Mono.empty();
        };
    }

    Publisher<Request> handle(PaymentResponse response);

    Publisher<Request> handle(InventoryResponse response);

}

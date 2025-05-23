package com.kyn.order.message.orchestrator;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.OrderFulfillmentService;
import com.kyn.order.message.orchestrator.interfaces.InventoryStep;
import com.kyn.order.message.orchestrator.interfaces.OrderFulfillmentOrchestrator;
import com.kyn.order.message.orchestrator.interfaces.PaymentStep;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentOrchestratorImpl implements OrderFulfillmentOrchestrator {

    private final PaymentStep paymentStep;
    private final InventoryStep inventoryStep;
    private final OrderFulfillmentService service;
    private final EventPublisher<UUID> eventPublisher;
    private WorkFlow workflow;

    @PostConstruct
    private void init() {
        this.workflow = WorkFlow.startWith(paymentStep)
                                .thenNext(inventoryStep)
                                .doOnFailure(id -> this.service.cancel(id).then())
                                .doOnSuccess(id -> this.service.complete(id).then()); // last step. or create it as builder
    }

    @Override
    public Publisher<Request> orderInitialRequests() {
        return this.eventPublisher.publish()
                                  .flatMap(this.workflow.getFirstStep()::send);
    }

    @Override
    public Publisher<Request> handle(PaymentResponse response) {
        return this.paymentStep.process(response);
    }

    @Override
    public Publisher<Request> handle(InventoryResponse response) {
        return this.inventoryStep.process(response);
    }


}

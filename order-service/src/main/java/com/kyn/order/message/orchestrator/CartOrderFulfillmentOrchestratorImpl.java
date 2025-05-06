package com.kyn.order.message.orchestrator;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.CartResponse;
import com.kyn.common.messages.Request;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.CartOrderFulfillmentService;
import com.kyn.order.common.service.OrderFulfillmentService;
import com.kyn.order.message.orchestrator.interfaces.CartInventoryStep;
import com.kyn.order.message.orchestrator.interfaces.CartOrderFulfillmentOrchestrator;
import com.kyn.order.message.orchestrator.interfaces.CartPaymentStep;
import com.kyn.order.message.orchestrator.interfaces.InventoryStep;
import com.kyn.order.message.orchestrator.interfaces.PaymentStep;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartOrderFulfillmentOrchestratorImpl implements CartOrderFulfillmentOrchestrator {
    private final CartPaymentStep paymentStep;
    private final CartInventoryStep inventoryStep;
    private final CartOrderFulfillmentService service;
    private final EventPublisher<OrderSummaryDto> eventPublisher;
    private CartWorkFlow workflow;

    @PostConstruct
    private void init() {
        this.workflow = CartWorkFlow.startWith(paymentStep)
                                .thenNext(inventoryStep)
                                .doOnFailure(id -> this.service.cancel(id).then())
                                .doOnSuccess(id -> this.service.complete(id).then()); // last step. or create it as builder
    }
    @Override
    public Publisher<CartRequest> orderInitialRequests() {
        return this.eventPublisher.publish()
            .flatMap(this.workflow.getFirstStep()::send);
    }
    @Override
    public Publisher<CartRequest> handle(CartPaymentResponse response) {
        return this.paymentStep.process(response);  
    }
    @Override
    public Publisher<CartRequest> handle(CartInventoryResponse response) {
        return this.inventoryStep.process(response);
    }
    
    
}

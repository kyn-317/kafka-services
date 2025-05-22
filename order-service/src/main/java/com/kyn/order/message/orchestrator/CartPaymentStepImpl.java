package com.kyn.order.message.orchestrator;



import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.orchestrator.CartRequestCompensator;
import com.kyn.common.orchestrator.CartRequestSender;
import com.kyn.order.common.enums.WorkflowAction;
import com.kyn.order.common.service.CartOrderFulfillmentService;
import com.kyn.order.common.service.CartWorkflowActionTracker;
import com.kyn.order.message.mapper.CartMessageDtoMapper;
import com.kyn.order.message.orchestrator.interfaces.CartPaymentStep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartPaymentStepImpl implements CartPaymentStep {

    private final CartWorkflowActionTracker tracker;
    private final CartOrderFulfillmentService service;
    private CartRequestCompensator previousStep;
    private CartRequestSender nextStep;

    @Override
    public Publisher<CartRequest> compensate(OrderSummaryDto dto) {
        return this.tracker.track(dto.getOrderId(), WorkflowAction.PAYMENT_REFUND_INITIATED)
                           .<CartRequest>thenReturn(CartMessageDtoMapper.toPaymentRefundRequest(dto))
                           .concatWith(this.previousStep.compensate(dto));
    }

    @Override
    public Publisher<CartRequest> send(OrderSummaryDto dto) {
        return this.tracker.track(dto.getOrderId(), WorkflowAction.PAYMENT_REQUEST_INITIATED)
                           .then(this.service.get(dto.getOrderId()))
                           .map(CartMessageDtoMapper::toPaymentProcessRequest);
    }

    @Override
    public void setPreviousStep(CartRequestCompensator previousStep) {
        this.previousStep = previousStep;
    }

    @Override
    public void setNextStep(CartRequestSender nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public Publisher<CartRequest> onSuccess(CartPaymentResponse.Processed response) {
        return this.tracker.track(response.responseItem().getOrderId(), WorkflowAction.PAYMENT_PROCESSED)
                           .thenMany(this.nextStep.send(response.responseItem()));
    }

    @Override
    public Publisher<CartRequest> onFailure(CartPaymentResponse.Declined response) {
        return this.tracker.track(response.responseItem().getOrderId(), WorkflowAction.PAYMENT_DECLINED)
                           .thenMany(this.previousStep.compensate(response.responseItem()));
    }
}

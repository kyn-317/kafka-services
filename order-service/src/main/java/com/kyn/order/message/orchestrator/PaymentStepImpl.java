package com.kyn.order.message.orchestrator;

import java.util.UUID;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.orchestrator.RequestCompensator;
import com.kyn.common.orchestrator.RequestSender;
import com.kyn.order.common.enums.WorkflowAction;
import com.kyn.order.common.service.OrderFulfillmentService;
import com.kyn.order.common.service.WorkflowActionTracker;
import com.kyn.order.message.mapper.MessageDtoMapper;
import com.kyn.order.message.orchestrator.interfaces.PaymentStep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentStepImpl implements PaymentStep {

    private final WorkflowActionTracker tracker;
    private final OrderFulfillmentService service;
    private RequestCompensator previousStep;
    private RequestSender nextStep;

    @Override
    public Publisher<Request> compensate(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.PAYMENT_REFUND_INITIATED)
                           .<Request>thenReturn(MessageDtoMapper.toPaymentRefundRequest(orderId))
                           .concatWith(this.previousStep.compensate(orderId));
    }

    @Override
    public Publisher<Request> send(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.PAYMENT_REQUEST_INITIATED)
                           .then(this.service.get(orderId))
                           .map(MessageDtoMapper::toPaymentProcessRequest);
    }

    @Override
    public void setPreviousStep(RequestCompensator previousStep) {
        this.previousStep = previousStep;
    }

    @Override
    public void setNextStep(RequestSender nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public Publisher<Request> onSuccess(PaymentResponse.Processed response) {
        return this.tracker.track(response.orderId(), WorkflowAction.PAYMENT_PROCESSED)
                           .thenMany(this.nextStep.send(response.orderId()));
    }

    @Override
    public Publisher<Request> onFailure(PaymentResponse.Declined response) {
        return this.tracker.track(response.orderId(), WorkflowAction.PAYMENT_DECLINED)
                           .thenMany(this.previousStep.compensate(response.orderId()));
    }
}

package com.kyn.order.message.orchestrator;

import java.util.UUID;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.Request;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.messages.inventory.CartInventoryResponse.Declined;
import com.kyn.common.messages.inventory.CartInventoryResponse.Deducted;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.orchestrator.CartRequestCompensator;
import com.kyn.common.orchestrator.CartRequestSender;
import com.kyn.common.orchestrator.RequestCompensator;
import com.kyn.common.orchestrator.RequestSender;
import com.kyn.order.application.mapper.CartMapper;
import com.kyn.order.common.enums.WorkflowAction;
import com.kyn.order.common.service.CartOrderFulfillmentService;
import com.kyn.order.common.service.CartWorkflowActionTracker;
import com.kyn.order.message.mapper.CartMessageDtoMapper;
import com.kyn.order.message.orchestrator.interfaces.CartInventoryStep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartInventoryStepImpl implements CartInventoryStep {

    private final CartWorkflowActionTracker tracker;
    private final CartOrderFulfillmentService service;
    private CartRequestCompensator previousStep;
    private CartRequestSender nextStep;

    @Override
    public Publisher<CartRequest> compensate(OrderSummaryDto dto) {
        return this.tracker.track(dto.getOrderId(), WorkflowAction.INVENTORY_RESTORE_INITIATED)
                           .<CartRequest>thenReturn(CartMessageDtoMapper.toInventoryRestoreRequest(dto))
                           .concatWith(this.previousStep.compensate(dto));
    }

    @Override
    public Publisher<CartRequest> send(OrderSummaryDto dto) {
        return this.tracker.track(dto.getOrderId(), WorkflowAction.INVENTORY_REQUEST_INITIATED)
                           .then(this.service.get(dto.getOrderId()))
                           .map(CartMessageDtoMapper::toInventoryDeductRequest);
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
    public Publisher<CartRequest> onSuccess(Deducted response) {
        return this.tracker.track(response.responseItem().getOrderId(), WorkflowAction.INVENTORY_DEDUCTED)
                           .thenMany(this.nextStep.send(response.responseItem()));
    }

    @Override
    public Publisher<CartRequest> onFailure(Declined response) {
        return this.tracker.track(response.responseItem().getOrderId(), WorkflowAction.INVENTORY_DECLINED)
                           .thenMany(this.previousStep.compensate(response.responseItem()));
    }
}

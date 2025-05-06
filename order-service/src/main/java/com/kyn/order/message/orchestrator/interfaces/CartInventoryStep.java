package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.orchestrator.CartWorkFlowStep;
import com.kyn.order.common.dto.OrderSummary;

public interface CartInventoryStep extends CartWorkFlowStep<CartInventoryResponse> {
    

    @Override
    default Publisher<Request> process(CartInventoryResponse response) {
        return switch (response) {
            case CartInventoryResponse.Deducted r -> this.onSuccess(r);
            case CartInventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(CartInventoryResponse.Deducted response);

    Publisher<Request> onFailure(CartInventoryResponse.Declined response);
    
    
}

package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.orchestrator.CartWorkFlowStep;

public interface CartInventoryStep extends CartWorkFlowStep<CartInventoryResponse> {
    

    @Override
    default Publisher<CartRequest> process(CartInventoryResponse response) {
        return switch (response) {
            case CartInventoryResponse.Deducted r -> this.onSuccess(r);
            case CartInventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<CartRequest> onSuccess(CartInventoryResponse.Deducted response);

    Publisher<CartRequest> onFailure(CartInventoryResponse.Declined response);
    
    
}

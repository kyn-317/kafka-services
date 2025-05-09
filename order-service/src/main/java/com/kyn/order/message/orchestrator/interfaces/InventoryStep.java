package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.orchestrator.WorkFlowStep;

public interface InventoryStep extends WorkFlowStep<InventoryResponse> {

    @Override
    default Publisher<Request> process(InventoryResponse response) {
        return switch (response){
            case InventoryResponse.Deducted r -> this.onSuccess(r);
            case InventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(InventoryResponse.Deducted response);

    Publisher<Request> onFailure(InventoryResponse.Declined response);

    
}

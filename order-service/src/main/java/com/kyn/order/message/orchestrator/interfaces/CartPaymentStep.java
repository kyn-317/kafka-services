package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.orchestrator.CartWorkFlowStep;

public interface CartPaymentStep extends CartWorkFlowStep<CartPaymentResponse> {
    @Override
    default Publisher<CartRequest> process(CartPaymentResponse response) {
        return switch (response){
            case CartPaymentResponse.Processed r -> this.onSuccess(r);
            case CartPaymentResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<CartRequest> onSuccess(CartPaymentResponse.Processed response);

    Publisher<CartRequest> onFailure(CartPaymentResponse.Declined response);

}

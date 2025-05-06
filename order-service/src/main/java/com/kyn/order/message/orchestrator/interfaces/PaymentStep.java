package com.kyn.order.message.orchestrator.interfaces;

import org.reactivestreams.Publisher;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.orchestrator.WorkFlowStep;

public interface PaymentStep extends WorkFlowStep<PaymentResponse> {

    @Override
    default Publisher<Request> process(PaymentResponse response) {
        return switch (response){
            case PaymentResponse.Processed r -> this.onSuccess(r);
            case PaymentResponse.Declined r -> this.onFailure(r);
            case PaymentResponse.Pending r -> this.onSuccessCart(r);
        };
    }

    Publisher<Request> onSuccess(PaymentResponse.Processed response);

    Publisher<Request> onFailure(PaymentResponse.Declined response);

    Publisher<Request> onSuccessCart(PaymentResponse.Pending response);
}

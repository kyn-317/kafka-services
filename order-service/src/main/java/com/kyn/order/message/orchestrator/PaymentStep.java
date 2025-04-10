package com.kyn.order.message.orchestrator;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.common.orchestrator.WorkFlowStep;
import org.reactivestreams.Publisher;

public interface PaymentStep extends WorkFlowStep<PaymentResponse> {

    @Override
    default Publisher<Request> process(PaymentResponse response) {
        return switch (response){
            case PaymentResponse.Processed r -> this.onSuccess(r);
            case PaymentResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(PaymentResponse.Processed response);

    Publisher<Request> onFailure(PaymentResponse.Declined response);

}

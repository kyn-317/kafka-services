package com.kyn.common.orchestrator;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.CartResponse;
import org.reactivestreams.Publisher;

public interface CartResponseProcessor<T extends CartResponse> {

    Publisher<CartRequest> process(T response);

}

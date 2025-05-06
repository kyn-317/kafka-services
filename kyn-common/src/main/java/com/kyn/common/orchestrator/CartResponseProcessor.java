package com.kyn.common.orchestrator;

import com.kyn.common.messages.CartResponse;
import com.kyn.common.messages.Request;
import org.reactivestreams.Publisher;

public interface CartResponseProcessor<T extends CartResponse> {

    Publisher<Request> process(T response);

}

package com.kyn.common.orchestrator;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.Response;
import org.reactivestreams.Publisher;

public interface ResponseProcessor<T extends Response> {

    Publisher<Request> process(T response);

}

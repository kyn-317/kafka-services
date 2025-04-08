package com.kyn.common.processor;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.Response;
import reactor.core.publisher.Mono;

public interface RequestProcessor<T extends Request, R extends Response> {

    Mono<R> process(T request);

}

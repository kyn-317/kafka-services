package com.kyn.common.processor;

import com.kyn.common.messages.MapRequest;
import com.kyn.common.messages.MapResponse;

import reactor.core.publisher.Mono;

public interface MapRequestProcessor<T extends MapRequest , R extends MapResponse> {

    Mono<Void> process(T request);

}

package com.kyn.common.processor;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.CartResponse;

import reactor.core.publisher.Mono;

public interface  CartRequestProcessor <T extends CartRequest, R extends CartResponse> {

    Mono<R> process(T request);

}
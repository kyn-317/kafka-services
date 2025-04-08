package com.kyn.common.publisher;

import reactor.core.publisher.Flux;

public interface EventPublisher<T> {

    Flux<T> publish();

}

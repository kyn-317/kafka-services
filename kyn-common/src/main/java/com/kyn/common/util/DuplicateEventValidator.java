package com.kyn.common.util;

import java.util.function.Function;

import com.kyn.common.exception.EventAlreadyProcessedException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class DuplicateEventValidator {

    public static Function<Mono<Boolean>, Mono<Void>> emitErrorForRedundantProcessing() {
        return mono -> mono
                .flatMap(b -> {
                    log.info("Checking duplicate event: {}", b);
                    return b ? Mono.error(new EventAlreadyProcessedException()) : Mono.empty();
                })
                .doOnError(EventAlreadyProcessedException.class, ex -> log.warn("Duplicate event detected"))
                .then();
    }

    public static <T> Mono<T> validate(Mono<Boolean> eventValidationPublisher, Mono<T> eventProcessingPublisher){
        return eventValidationPublisher
                .doOnNext(data -> log.info("Validation result: {}", data))
                .transform(emitErrorForRedundantProcessing())
                .then(eventProcessingPublisher)
                .doOnNext(data -> log.info("Processing result: {}", data != null));
    }

    /*

            DuplicateEventValidator.validate(  isPresentInDB(some-id), process(some-id) )
                                .doOnNext(...)
                                .map(...)
                                ...
                                ...

     */


}

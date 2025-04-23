package com.kyn.order.message.publisher;

import java.time.Duration;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.MessageEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
public class MessageEventListenerImpl implements MessageEventListener, EventPublisher<MessageRequest> {

    private final Sinks.Many<MessageRequest> sink;
    private final Flux<MessageRequest> flux;


    @Override
    public Flux<MessageRequest> publish() {
        log.info("publish >> {}", this.flux);
        return this.flux;
    }

    @Override
    public void createMessage(MessageRequest message) {
        log.info("createMessage >> {}", message);
        this.sink.emitNext(
                message,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }
}
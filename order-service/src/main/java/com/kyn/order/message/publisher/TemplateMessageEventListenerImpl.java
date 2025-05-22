package com.kyn.order.message.publisher;

import java.time.Duration;

import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.TemplateMessageEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
public class TemplateMessageEventListenerImpl implements TemplateMessageEventListener, EventPublisher<TemplateMessageRequest> {

    private final Sinks.Many<TemplateMessageRequest> sink;
    private final Flux<TemplateMessageRequest> flux;

    
    @Override
    public Flux<TemplateMessageRequest> publish() {
        return this.flux;
    }

    @Override
    public void createMessage(TemplateMessageRequest message) {
        this.sink.emitNext(
            message,
            Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }
    
}

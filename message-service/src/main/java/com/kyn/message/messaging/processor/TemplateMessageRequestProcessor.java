package com.kyn.message.messaging.processor;

import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.common.messages.message.TemplateMessageResponse;
import com.kyn.common.processor.MapRequestProcessor;

import reactor.core.publisher.Mono;


public interface  TemplateMessageRequestProcessor extends MapRequestProcessor<TemplateMessageRequest, TemplateMessageResponse> {

    @Override
    default Mono<Void> process(TemplateMessageRequest request) {
        return switch (request){
            case TemplateMessageRequest.ORDER_COMPLETED s -> this.handle(s);
            case TemplateMessageRequest.ORDER_CANCELLED s -> this.handle(s);
            case TemplateMessageRequest.STOCK_RECEIVED s -> this.handle(s);
            default -> Mono.error(new IllegalArgumentException("Unsupported message request type"));
        };
    }

    Mono<Void> handle(TemplateMessageRequest.ORDER_COMPLETED request);
    Mono<Void> handle(TemplateMessageRequest.ORDER_CANCELLED request);
    Mono<Void> handle(TemplateMessageRequest.STOCK_RECEIVED request);
}

package com.kyn.message.messaging.processor;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.common.processor.RequestProcessor;

import reactor.core.publisher.Mono;


public interface MessageRequestProcessor extends RequestProcessor<MessageRequest, MessageResponse>{

    @Override
    default Mono<MessageResponse> process(MessageRequest request) {
        return switch (request){
            case MessageRequest.Push s -> this.handle(s);
            default -> Mono.error(new IllegalArgumentException("Unsupported message request type"));
        };
    }

    Mono<MessageResponse> handle(MessageRequest.Push request);
    
}
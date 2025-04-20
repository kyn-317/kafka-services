package com.kyn.message.messaging.processor;

import org.springframework.stereotype.Component;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.common.processor.RequestProcessor;
import com.kyn.message.application.service.interfaces.MessageService;

import reactor.core.publisher.Mono;


public interface MessageRequestProcessor extends RequestProcessor<MessageRequest, MessageResponse>{

    @Override
    default Mono<MessageResponse> process(MessageRequest request) {
        return switch (request){
            case MessageRequest.Push s -> this.handle(s);
        };
    }

    Mono<MessageResponse> handle(MessageRequest.Push request);

    
}
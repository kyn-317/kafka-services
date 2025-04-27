package com.kyn.message.messaging.processor;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.messaging.mapper.MessageDtoMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MessageRequestProcessorImpl implements MessageRequestProcessor{
    
    private final MessagingService messagingService;

    public MessageRequestProcessorImpl(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override   
    public Mono<MessageResponse> handle(MessageRequest.Push request) {
        log.info("message service received message from OrderService: orderId={}, userId={}, message='{}'", 
            request.orderId(), request.userId(), request.message());    
        return messagingService.push(request)
                .doOnSuccess(result -> log.info("message processing completed: messageId={}", result.messageId()))
                .map(MessageDtoMapper::toMessageResponse);
    }


}

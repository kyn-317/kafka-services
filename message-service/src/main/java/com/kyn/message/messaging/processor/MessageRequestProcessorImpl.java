package com.kyn.message.messaging.processor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.messaging.mapper.MessageDtoMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service

public class MessageRequestProcessorImpl implements MessageRequestProcessor{
    
    private final MessagingService messagingService;

    public MessageRequestProcessorImpl(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override   
    public Mono<MessageResponse> handle(MessageRequest.Push request) {
        var dto = MessageDtoMapper.toPushRequest(request);
        return messagingService.push(dto)
                             .map(MessageDtoMapper::toMessageResponse);
    }
}

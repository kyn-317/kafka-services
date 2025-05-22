package com.kyn.message.application.service;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.message.application.dto.MessageData;
import com.kyn.message.application.dto.ServerSentMessage;
import com.kyn.message.application.service.interfaces.MessageService;
import com.kyn.message.application.service.interfaces.MessagingService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

    private final MessageService messageService;

    public MessagingServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Mono<Void> push(String message, String userId, String type) {
        var serverSentMessage = ServerSentMessage.builder()
                .type(type)
                .data(MessageData.builder()
                        .message(message)
                        .build())
                .build();
        return messageService.sendEventToClient(userId, serverSentMessage);
    }
}

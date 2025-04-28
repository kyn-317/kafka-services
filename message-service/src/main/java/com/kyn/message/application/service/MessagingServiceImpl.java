package com.kyn.message.application.service;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.message.application.dto.MessageData;
import com.kyn.message.application.dto.ServerSentMessage;
import com.kyn.message.application.repository.MessageHistoryRepository;
import com.kyn.message.application.service.interfaces.MessageService;
import com.kyn.message.application.service.interfaces.MessagingService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

    private final MessageService messageService;
    private final MessageHistoryRepository messageHistoryRepository;

    public MessagingServiceImpl(MessageService messageService, MessageHistoryRepository messageHistoryRepository) {
        this.messageService = messageService;
        this.messageHistoryRepository = messageHistoryRepository;
    }


    @Override
    public Mono<Void> push(MessageRequest.Push request) {
        
        var serverSentMessage = ServerSentMessage.builder()
            .type("push")
            .data(MessageData.builder()
                .message(request)
                .build())
            .build();
        
        // 클라이언트에 이벤트만 전송하고 즉시 반환
        return messageService.sendEventToClient(request.userId().toString(), serverSentMessage);
    }
}

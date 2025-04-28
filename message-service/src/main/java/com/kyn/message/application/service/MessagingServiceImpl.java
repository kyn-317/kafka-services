package com.kyn.message.application.service;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.message.application.dto.MessageData;
import com.kyn.message.application.dto.ServerSentMessage;
import com.kyn.message.application.entity.MessageHistory;
import com.kyn.message.application.mapper.EntityDtoMapper;
import com.kyn.message.application.repository.MessageHistoryRepository;
import com.kyn.message.application.service.interfaces.MessageService;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.common.dto.MessageDto;

import reactor.core.publisher.Mono;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final MessageService messageService;
    private final MessageHistoryRepository messageHistoryRepository;

    public MessagingServiceImpl(MessageService messageService, MessageHistoryRepository messageHistoryRepository) {
        this.messageService = messageService;
        this.messageHistoryRepository = messageHistoryRepository;
    }


    @Override
    public Mono<MessageDto> push(MessageRequest.Push request) {
        
        var serverSentMessage = ServerSentMessage.builder()
            .type("push")
            .data(MessageData.builder()
                .message(request)
                .build())
            .build();

        var messageHistory = MessageHistory.builder()
            .userId(request.userId())
            .orderId(request.orderId())
            .message(request.message())
            .build();
        
        return messageService.sendEventToClient(request.userId().toString(), serverSentMessage)
        .then(messageHistoryRepository.save(messageHistory))
        .thenReturn(EntityDtoMapper.toDto(messageHistory));
    }
}

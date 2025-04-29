package com.kyn.message.messaging.processor;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.message.application.entity.MessageHistory;
import com.kyn.message.application.mapper.EntityDtoMapper;
import com.kyn.message.application.repository.MessageHistoryRepository;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.messaging.mapper.MessageDtoMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MessageRequestProcessorImpl implements MessageRequestProcessor{
    
    private final MessagingService messagingService;
    private final MessageHistoryRepository messageHistoryRepository;
    public MessageRequestProcessorImpl(MessagingService messagingService, MessageHistoryRepository messageHistoryRepository) {
        this.messagingService = messagingService;
        this.messageHistoryRepository = messageHistoryRepository;
    }

    @Override   
    public Mono<MessageResponse> handle(MessageRequest.Push request) {
        // initialize message history
        var messageHistory = MessageHistory.builder()
            ._id(UUID.randomUUID().toString())
            .userId(request.userId().toString())
            .orderId(request.orderId().toString())
            .message(request.message())
            .build();   
            messageHistory.insertDocument(request.userId().toString());
        
        // send message to client
        return messagingService.push(request)
            .then(Mono.defer(() -> messageHistoryRepository.save(messageHistory)
                .doOnSuccess(savedMessageHistory -> log.info("Message history saved: {}", savedMessageHistory))
                .map(EntityDtoMapper::toDto)
                .map(MessageDtoMapper::toMessageResponse)));
    }
}

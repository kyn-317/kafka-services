package com.kyn.message.messaging.processor;

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
        log.info("message service received message from OrderService: \norderId={}, userId={}, message='{}'", 
            request.orderId(), request.userId(), request.message()); 
            
        // 메시지 히스토리 객체 생성
        var messageHistory = MessageHistory.builder()
            .userId(request.userId())
            .orderId(request.orderId())
            .message(request.message())
            .build();   
        
        // 먼저 클라이언트에 이벤트 전송
        return messagingService.push(request)
            .then(Mono.defer(() -> messageHistoryRepository.save(messageHistory)
                .doOnSuccess(savedMessageHistory -> log.info("Message history saved: {}", savedMessageHistory))
                .map(EntityDtoMapper::toDto)
                .map(MessageDtoMapper::toMessageResponse)));
    }
}

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
        log.info("메시지 서비스가 OrderService로부터 메시지를 수신했습니다: orderId={}, userId={}, 메시지='{}'", 
            request.orderId(), request.userId(), request.message());
            
        var dto = MessageDtoMapper.toPushRequest(request);
        return messagingService.push(dto)
                .doOnSuccess(result -> log.info("메시지 처리 완료: messageId={}", result.messageId()))
                .map(MessageDtoMapper::toMessageResponse);
    }
}

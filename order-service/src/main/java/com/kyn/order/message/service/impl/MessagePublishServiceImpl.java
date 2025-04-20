package com.kyn.order.message.service.impl;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.order.message.config.MessagePublisherConfig.MessageSender;
import com.kyn.order.message.service.MessagePublishService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublishServiceImpl implements MessagePublishService {

    private final MessageSender messageSender;
    private final StreamBridge streamBridge;
    private static final String MESSAGE_REQUEST_BINDING = "message-request-channel";
    
    @Override
    public Mono<Void> publishOrderMessage(UUID orderId, UUID userId, String message) {
        log.info("Publishing order message: orderId={}, userId={}, message={}", orderId, userId, message);
        
        return Mono.fromSupplier(createMessageRequest(orderId, userId, message))
                .map(messageSender::toMessage)
                .flatMap(msg -> Mono.fromRunnable(() -> 
                    streamBridge.send(MESSAGE_REQUEST_BINDING, msg)))
                .then();
    }
    
    private Supplier<MessageRequest> createMessageRequest(UUID orderId, UUID userId, String message) {
        return () -> MessageRequest.Push.builder()
                .orderId(orderId)
                .userId(userId)
                .message(message)
                .build();
    }
} 
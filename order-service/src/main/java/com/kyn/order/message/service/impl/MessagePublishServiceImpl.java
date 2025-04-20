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
    // 'message-request'는 실제 Kafka 토픽 이름입니다.
    // StreamBridge에서는 토픽 이름을 직접 사용합니다.
    private static final String MESSAGE_REQUEST_TOPIC = "message-request";
    
    @Override
    public Mono<Void> publishOrderMessage(UUID orderId, UUID userId, String message) {
        log.info("메시지 발행 시작: orderId={}, userId={}, message={}", orderId, userId, message);
        
        return Mono.fromSupplier(createMessageRequest(orderId, userId, message))
                .doOnNext(req -> log.info("MessageRequest 생성: {}", req))
                .map(messageSender::toMessage)
                .doOnNext(msg -> log.info("Message 생성 완료: payload={}, headers={}", msg.getPayload(), msg.getHeaders()))
                .flatMap(msg -> Mono.fromCallable(() -> {
                    // 직접 토픽 이름을 사용하여 메시지 발행
                    boolean sent = streamBridge.send(MESSAGE_REQUEST_TOPIC, msg);
                    log.info("StreamBridge.send 결과: destination={}, success={}", MESSAGE_REQUEST_TOPIC, sent);
                    return sent;
                }))
                .doOnSuccess(success -> log.info("메시지 발행 완료: success={}", success))
                .doOnError(e -> log.error("메시지 발행 중 오류 발생: {}", e.getMessage(), e))
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
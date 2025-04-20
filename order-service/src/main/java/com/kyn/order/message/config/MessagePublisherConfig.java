package com.kyn.order.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.message.MessageRequest;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessagePublisherConfig {

    // Spring Cloud Stream의 메시지 destination 헤더
    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    // 실제 Kafka 토픽 이름을 직접 사용합니다
    private static final String MESSAGE_REQUEST_CHANNEL = "message-request-channel";

    @Bean
    public MessageSender messageSender() {
        return request -> {
            log.info("주문 서비스가 메시지 생성: type={}, orderId={}, userId={}", 
                    request.getClass().getSimpleName(), 
                    request.orderId(),
                    request instanceof MessageRequest.Push push ? push.userId() : "N/A");
                    
            return MessageBuilder.withPayload(request)
                    .setHeader(KafkaHeaders.KEY, request.orderId().toString())
                    .setHeader(DESTINATION_HEADER, getDestination(request))
                    .setHeader("contentType", "application/json")
                    .build();
        };
    }

    public interface MessageSender {
        Message<MessageRequest> toMessage(MessageRequest request);
    }

    private String getDestination(Request request) {
        return switch (request) {
            case MessageRequest r -> MESSAGE_REQUEST_CHANNEL;
            default -> throw new IllegalStateException("Unexpected value: " + request);
        };
    }

} 
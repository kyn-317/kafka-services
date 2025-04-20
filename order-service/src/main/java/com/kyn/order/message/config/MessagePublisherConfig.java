package com.kyn.order.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.message.MessageRequest;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessagePublisherConfig {

    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    private static final String MESSAGE_REQUEST_CHANNEL = "message-request-channel";

    @Bean
    public MessageSender messageSender() {
        return request -> {
            log.info("order service produced message request: {}", request);
            return MessageBuilder.withPayload(request)
                    .setHeader(KafkaHeaders.KEY, request.orderId().toString())
                    .setHeader(DESTINATION_HEADER, MESSAGE_REQUEST_CHANNEL)
                    .build();
        };
    }

    public interface MessageSender {
        Message<MessageRequest> toMessage(MessageRequest request);
    }
} 
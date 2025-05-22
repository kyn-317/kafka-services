package com.kyn.order.message.producer;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.TemplateMessageEventListener;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration

public class TemplateMessageEventProducer {
    
    private final TemplateMessageEventListener templateMessageEventListener;
    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    public TemplateMessageEventProducer(TemplateMessageEventListener templateMessageEventListener){
        this.templateMessageEventListener = templateMessageEventListener;
    }
    @Bean
    public Supplier<Flux<Message<TemplateMessageRequest>>> templateMessageEventSupplier() {
        return () -> ((EventPublisher<TemplateMessageRequest>)templateMessageEventListener).publish()
                    .map(this::toMessage)
                    .doOnNext(this::printDetails);
    }

    private Message<TemplateMessageRequest> toMessage(TemplateMessageRequest message){
        return MessageBuilder.withPayload(message)
        .setHeader(KafkaHeaders.KEY, message.userId().toString())
        .setHeader(DESTINATION_HEADER, getDestination(message))
        .build();
    }
    private String getDestination(TemplateMessageRequest message){
        return switch (message) {
            case TemplateMessageRequest.ORDER_COMPLETED r -> "template-message-request-channel";
            case TemplateMessageRequest.ORDER_CANCELLED r -> "template-message-request-channel";
            case TemplateMessageRequest.STOCK_RECEIVED r -> "template-message-request-channel";
            default -> throw new IllegalArgumentException("Unknown message type: " + message);
        };
    }

    private void printDetails(Message<TemplateMessageRequest> message){
        log.info("message event producer >>{}", message.getPayload());
        log.info("message event headers >>{}", message.getHeaders());
        log.info("message event key >>{}", message.getHeaders().get(KafkaHeaders.KEY));
        log.info("message event destination >>{}", message.getHeaders().get(DESTINATION_HEADER));
    }
}

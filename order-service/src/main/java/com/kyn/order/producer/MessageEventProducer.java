package com.kyn.order.producer;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.MessageEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration

public class MessageEventProducer {
    
    private final MessageEventListener messageEventListener;
    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    public MessageEventProducer(MessageEventListener messageEventListener){
        this.messageEventListener = messageEventListener;
    }
    @Bean
    public Supplier<Flux<Message<MessageRequest.Push>>> messageEventSupplier() {
        return () -> ((EventPublisher<MessageRequest.Push>)messageEventListener).publish()
                    .map(this::toMessage)
                    .doOnNext(this::printDetails);
    }
    private Message<MessageRequest.Push> toMessage(MessageRequest.Push message){
        return MessageBuilder.withPayload(message)
        .setHeader(KafkaHeaders.KEY, message.orderId().toString())
        .setHeader(DESTINATION_HEADER, getDestination(message))
        .build();
    }
    private String getDestination(MessageRequest.Push message){
        return switch (message) {
            case MessageRequest.Push r -> "message-request-channel";
        
        };
    }

    private void printDetails(Message<MessageRequest.Push> message){
        log.info("message event producer >>{}", message.getPayload());
        log.info("message event headers >>{}", message.getHeaders());
        log.info("message event key >>{}", message.getHeaders().get(KafkaHeaders.KEY));
        log.info("message event destination >>{}", message.getHeaders().get(DESTINATION_HEADER));
    }
}

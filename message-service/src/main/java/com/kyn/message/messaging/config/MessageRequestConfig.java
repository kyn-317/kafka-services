package com.kyn.message.messaging.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.common.util.MessageConverter;
import com.kyn.common.util.Record;
import com.kyn.message.messaging.processor.MessageRequestProcessor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
@Configuration
@Slf4j
public class MessageRequestConfig {

    private final MessageRequestProcessor messageRequestProcessor;
    public MessageRequestConfig(MessageRequestProcessor messageRequestProcessor) {
        this.messageRequestProcessor = messageRequestProcessor;
    }

    @Bean
    public Function<Flux<Message<MessageRequest>>, Flux<Message<MessageResponse>>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
        .flatMap(r -> this.messageRequestProcessor.process(r.message())
        .doOnSuccess(e -> r.acknowledgement().acknowledge()))
        .map(this::toMessage);
    }
        

    protected Message<MessageResponse> toMessage(MessageResponse response) {
        log.info("message service produced {}", response);
        return MessageBuilder.withPayload(response)
                             .setHeader(KafkaHeaders.KEY, response.orderId().toString())
                             .build();
    }
    
}

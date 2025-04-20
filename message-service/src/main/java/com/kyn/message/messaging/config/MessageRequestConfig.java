package com.kyn.message.messaging.config;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.common.util.MessageConverter;
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
    public Consumer<Flux<Message<MessageRequest>>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("shipping service received {}", r.message()))
                           .concatMap(r -> this.messageRequestProcessor.process(r.message())
                                                                       .doOnSuccess(e -> r.acknowledgement().acknowledge())
                                                                       .doOnError(e -> log.error(e.getMessage()))
                           );
    }
}

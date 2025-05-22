package com.kyn.order.application.consumer;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.util.MessageConverter;
import com.kyn.common.util.Record;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class MessageConsumerConfig {
    
    @Bean
    public Function<Flux<Message<MessageRequest.Push>>, Mono<Void>> messageConsumer(){
        return flux -> flux.map(MessageConverter::toRecord)
        .doOnNext(this::printDetails)
        .then();
    }

    private void printDetails(Record<MessageRequest.Push> record){
        log.info("message message {}", record.message());
        log.info("message ack {}", record.acknowledgement());
        log.info("message key {}", record.key());
        record.acknowledgement().acknowledge();
    }
}
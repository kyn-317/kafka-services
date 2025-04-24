package com.kyn.message.messaging.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.util.MessageConverter;
import com.kyn.common.util.Record;
import com.kyn.message.messaging.processor.MessageRequestProcessor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Configuration
@Slf4j
public class MessageRequestConfig {

    private final MessageRequestProcessor messageRequestProcessor;
    

    public MessageRequestConfig(MessageRequestProcessor messageRequestProcessor) {
        this.messageRequestProcessor = messageRequestProcessor;
    }

    @Bean
    public Function<Flux<Message<MessageRequest>>, Mono<Void>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
        .doOnNext(this::printDetails)
        .doOnNext(r -> log.info("message service received message: type={}, orderId={}", 
        r.message().getClass().getSimpleName(),
        r.message().orderId()))
        .then();
/*         return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("message service received message: type={}, orderId={}", 
                                                r.message().getClass().getSimpleName(),
                                                r.message().orderId()))
                           .concatMap(r -> this.messageRequestProcessor.process(r.message())
                                                                       .doOnSuccess(e -> {
                                                                           log.info("message processing completed: orderId={}", r.message().orderId());
                                                                           r.acknowledgement().acknowledge();
                                                                       })
                                                                       .doOnError(e -> log.error("message processing error: {}", e.getMessage()))
                           ).then(); */
    }


    private void printDetails(Record<MessageRequest> record){
        log.info("message message {}", record.message());
        log.info("message ack {}", record.acknowledgement());
        log.info("message key {}", record.key());
        record.acknowledgement().acknowledge();
    }

    
}

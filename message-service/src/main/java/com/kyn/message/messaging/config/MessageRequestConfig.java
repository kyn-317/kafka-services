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
                           .doOnNext(r -> log.info("메시지 서비스가 메시지를 수신했습니다: type={}, orderId={}", 
                                                r.message().getClass().getSimpleName(),
                                                r.message().orderId()))
                           .concatMap(r -> this.messageRequestProcessor.process(r.message())
                                                                       .doOnSuccess(e -> {
                                                                           log.info("메시지 처리가 완료되었습니다: orderId={}", r.message().orderId());
                                                                           r.acknowledgement().acknowledge();
                                                                       })
                                                                       .doOnError(e -> log.error("메시지 처리 중 오류 발생: {}", e.getMessage()))
                           );
    }

    
}

package com.kyn.message.messaging.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.common.util.MessageConverter;
import com.kyn.message.messaging.processor.TemplateMessageRequestProcessor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;


@Configuration
@Slf4j  
public class TemplateMessageRequestConfig {

    private final TemplateMessageRequestProcessor templateMessageRequestProcessor;

    public TemplateMessageRequestConfig(TemplateMessageRequestProcessor templateMessageRequestProcessor) {
        this.templateMessageRequestProcessor = templateMessageRequestProcessor;
    }

    @Bean
    public Consumer<Flux<Message<TemplateMessageRequest>>> consumer() {
        return flux -> flux.map(MessageConverter::toRecord)
            .flatMap(record -> this.templateMessageRequestProcessor.process(record.message())
            .doOnSuccess(response ->record.acknowledgement().acknowledge()))
            .subscribe();
    }
    
}

package com.kyn.message.messaging.config;

import java.util.function.Function;
import java.time.Duration;

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
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

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
            .flatMap(record -> {
                return this.messageRequestProcessor.process(record.message())
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .doBeforeRetry(signal -> 
                            log.info("Retrying message processing: {}", record.message().orderId())))
                    .doOnSuccess(response -> {
                        try {
                            record.acknowledgement().acknowledge();
                            log.info("Message processed and acknowledged: {}", record.message().orderId());
                        } catch (Exception e) {
                            log.error("Error acknowledging message: {}", e.getMessage());
                        }
                    })
                    .doOnError(error -> {
                        log.error("Error processing message: {}", error.getMessage());
                        // DLQ로 전송하는 로직 추가
                    })
                    .onErrorResume(error -> {
                        log.error("Error in message processing pipeline: {}", error.getMessage());
                        return Mono.empty();
                    });
            })
            .map(this::toMessage);
    }
        

    protected Message<MessageResponse> toMessage(MessageResponse response) {
        log.info("message service produced {}", response);
        return MessageBuilder.withPayload(response)
                             .setHeader(KafkaHeaders.KEY, response.orderId().toString())
                             .build();
    }
    
}

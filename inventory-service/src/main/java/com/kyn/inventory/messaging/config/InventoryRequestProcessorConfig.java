package com.kyn.inventory.messaging.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.util.MessageConverter;
import com.kyn.inventory.messaging.processor.InventoryRequestProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InventoryRequestProcessorConfig {
    
    private final InventoryRequestProcessor inventoryRequestProcessor;
    @Bean
    public Function<Flux<Message<CartInventoryRequest>>, Flux<Message<CartInventoryResponse>>> cartInventoryRequestProcessor() {
        return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("inventory service received {}", r.message()))
                           .concatMap(r -> this.inventoryRequestProcessor.process(r.message())
                                                                       .doOnSuccess(e -> r.acknowledgement().acknowledge())
                                                                       .doOnError(e -> log.error(e.getMessage()))
                           )
                           .map(this::toMessage);
    }

    private Message<CartInventoryResponse> toMessage(CartInventoryResponse response) {
        log.info("inventory service produced {}", response);
        return MessageBuilder.withPayload(response)
                             .setHeader(KafkaHeaders.KEY, response.responseItem().getOrderId().toString())
                             .build();
    }
}

package com.kyn.payment.messaging.config;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.util.MessageConverter;
import com.kyn.payment.messaging.processor.CartPaymentRequestProcessor;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Configuration
@RequiredArgsConstructor
public class CartPaymentRequestProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(CartPaymentRequestProcessorConfig.class);
    private final CartPaymentRequestProcessor paymentRequestProcessor;

    @Bean
    public Function<Flux<Message<CartPaymentRequest>>, Flux<Message<CartPaymentResponse>>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("customer payment received {}", r.message()))
                           .concatMap(r -> this.paymentRequestProcessor.process(r.message())
                                                                       .doOnSuccess(e -> r.acknowledgement().acknowledge())
                                                                       .doOnError(e -> log.error(e.getMessage()))
                           )
                           .map(this::toMessage);
    }

    private Message<CartPaymentResponse> toMessage(CartPaymentResponse response) {
        log.info("customer payment produced {}", response);
        return MessageBuilder.withPayload(response)
                             .setHeader(KafkaHeaders.KEY, response.responseItem().getOrderId().toString())
                             .build();
    }

}
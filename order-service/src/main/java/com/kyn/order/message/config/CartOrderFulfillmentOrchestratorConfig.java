package com.kyn.order.message.config;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.CartResponse;
import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.common.util.MessageConverter;
import com.kyn.order.message.orchestrator.interfaces.CartOrderFulfillmentOrchestrator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Configuration
@RequiredArgsConstructor
public class CartOrderFulfillmentOrchestratorConfig {

    private static final Logger log = LoggerFactory.getLogger(CartOrderFulfillmentOrchestratorConfig.class);
    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    private static final String PAYMENT_REQUEST_CHANNEL = "cart-payment-request-channel";
    private static final String INVENTORY_REQUEST_CHANNEL = "cart-inventory-request-channel";
    private final CartOrderFulfillmentOrchestrator orchestrator;

    @Bean
    public Function<Flux<Message<CartResponse>>, Flux<Message<CartRequest>>> cartOrderOrchestrator() {
        return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("order service received {}", r.message()))
                           .concatMap(r -> Flux.from(orchestrator.orchestrate(r.message()))
                                               .doAfterTerminate(() -> r.acknowledgement().acknowledge())
                           )
                           .mergeWith(orchestrator.orderInitialRequests())
                           .map(this::toMessage);
    }

    protected Message<CartRequest> toMessage(CartRequest request) {
        log.info("order service produced {}", request);
        return MessageBuilder.withPayload(request)
                             .setHeader(KafkaHeaders.KEY, request.requestItem().getOrderId().toString())
                             .setHeader(DESTINATION_HEADER, getDestination(request))
                             .build();
    }

    private String getDestination(CartRequest request) {
        return switch (request) {
            case CartPaymentRequest r -> PAYMENT_REQUEST_CHANNEL;
            case CartInventoryRequest r -> INVENTORY_REQUEST_CHANNEL;
            default -> throw new IllegalStateException("Unexpected value: " + request);
        };
    }

}

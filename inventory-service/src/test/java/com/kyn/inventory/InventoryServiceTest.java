/* 
package com.kyn.inventory;


import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import com.kyn.common.dto.OrderDetailDto;
import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@TestPropertySource(properties = {
        "spring.cloud.function.definition=processor;responseConsumer",
        "spring.cloud.stream.bindings.responseConsumer-in-0.destination=inventory-response"
})
public class InventoryServiceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<CartInventoryResponse> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<CartInventoryResponse> resFlux = resSink.asFlux().cache(0);




    @Test
    public void deductAndRestoreTest(){
        var orderDetails = List.of(
            OrderDetailDto.builder()
            .productId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
            .quantity(10)
            .build(),
            OrderDetailDto.builder()
            .productId(UUID.fromString("44444444-4444-4444-4444-444444444444"))
            .quantity(10)
            .build()
        );
        var exampleOrder = OrderSummaryDto.builder()
        .orderId(UUID.randomUUID())
        .customerId(UUID.randomUUID())
        .orderDetails(orderDetails)
        .build();

        var request = CartInventoryRequest.Deduct.builder()
        .requestItem(exampleOrder)
        .build();

        expectResponse(request, CartInventoryResponse.class, res -> {
            Assertions.assertThat(res.responseItem().getOrderId()).isEqualTo(exampleOrder.getOrderId());
            Assertions.assertThat(res.responseItem().getCustomerId()).isEqualTo(exampleOrder.getCustomerId());
            Assertions.assertThat(res.responseItem().getOrderDetails().size()).isEqualTo(exampleOrder.getOrderDetails().size());
            Assertions.assertThat(res.responseItem().getOrderDetails().get(0).getProductId()).isEqualTo(exampleOrder.getOrderDetails().get(0).getProductId());
            Assertions.assertThat(res.responseItem().getOrderDetails().get(0).getQuantity()).isEqualTo(exampleOrder.getOrderDetails().get(0).getQuantity());
        });
    }

    @Test // please remove this - not a good fit for embedded kafka test. should be covered as part of unit tests
    public void restoreWithoutDeductTest(){
    }

    @Test
    public void outOfStockErrorTest(){
    }

    private <T> void expectResponse(CartInventoryRequest request, Class<T> type, Consumer<T> assertion){
        resFlux
                .doFirst(() -> this.streamBridge.send("inventory-request", request))
                .timeout(Duration.ofSeconds(2), Mono.empty())
                .cast(type)
                .as(StepVerifier::create)
                .consumeNextWith(assertion)
                .verifyComplete();
    }

    private void expectNoResponse(CartInventoryRequest request){
        resFlux
                .doFirst(() -> this.streamBridge.send("inventory-request", request))
                .timeout(Duration.ofSeconds(2), Mono.empty())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Consumer<Flux<CartInventoryResponse>> responseConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }

    }
    
}
*/
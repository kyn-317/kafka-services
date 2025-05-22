package com.kyn.payment;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.payment.application.entity.Account;
import com.kyn.payment.application.repository.AccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
@TestPropertySource(properties = {
        "spring.cloud.function.definition=processor;responseConsumer",
        "spring.cloud.stream.bindings.responseConsumer-in-0.destination=payment-response"
})
public class PaymentServiceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<CartPaymentResponse> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<CartPaymentResponse> resFlux = resSink.asFlux().cache(0);

    @Autowired
    private AccountRepository repository;

    @Autowired
    private StreamBridge streamBridge;

    private Account getAccount(String email){
        return this.repository.findByEmail(email).block();
    }

    @Test
    public void processAndRefundTest(){

        var orderId = UUID.randomUUID();
        var account = getAccount("sam@gmail.com");
        var processRequest = TestDataUtil.createProcessRequest(orderId, account.getId(), 3.0);
        var refundRequest = TestDataUtil.createRefundRequest(orderId);
        
        // process payment
        expectResponse(processRequest, CartPaymentResponse.Processed.class, e -> {
            Assertions.assertNotNull(e.paymentId());
            Assertions.assertEquals(orderId, e.responseItem().getOrderId());
            Assertions.assertEquals(3.0, e.responseItem().getTotalPrice());
        });

        // check balance
        this.repository.findById(account.getId())
                       .as(StepVerifier::create)
                       .consumeNextWith(c -> Assertions.assertEquals(97, c.getBalance()))
                       .verifyComplete();


        // duplicate request
        expectNoResponse(processRequest);

        // refund request
        expectNoResponse(refundRequest);

        // check balance
        this.repository.findById(account.getId())
                       .as(StepVerifier::create)
                       .consumeNextWith(c -> Assertions.assertEquals(100, c.getBalance()))
                       .verifyComplete();

    }

    @Test // please remove this. should be covered as part of unit tests
    public void refundWithoutProcessTest(){
        var orderId = UUID.randomUUID();
        var refundRequest = TestDataUtil.createRefundRequest(orderId);
        expectNoResponse(refundRequest);
        this.repository.findAll()
                       .map(Account::getBalance)
                       .distinct()
                       .as(StepVerifier::create)
                       .consumeNextWith(b -> Assertions.assertEquals(100, b))
                       .verifyComplete();
    }

    @Test// test case for customer not found
    public void customerNotFoundTest(){
        var orderId = UUID.randomUUID();
        var processRequest = TestDataUtil.createProcessRequest(orderId, UUID.randomUUID(), 3.0);
        expectResponse(processRequest, CartPaymentResponse.Declined.class, e -> {
            Assertions.assertEquals(orderId, e.responseItem().getOrderId());
            Assertions.assertEquals("Customer not found", e.message());
        });
    }

    @Test// test case for insufficient balance
    public void insufficientBalanceTest(){
        var orderId = UUID.randomUUID();
        var account = getAccount("mike@gmail.com");
        var processRequest = TestDataUtil.createProcessRequest(orderId, account.getId(), 101.0);
        expectResponse(processRequest, CartPaymentResponse.Declined.class, e -> {
            Assertions.assertEquals(orderId, e.responseItem().getOrderId());
            Assertions.assertEquals("Customer does not have enough balance", e.message());
        });
    }

    private <T> void expectResponse(CartRequest request, Class<T> type, Consumer<T> assertion){
        resFlux
                .doFirst(() -> this.streamBridge.send("payment-request", request))
                .timeout(Duration.ofSeconds(2), Mono.empty())
                .cast(type)
                .as(StepVerifier::create)
                .consumeNextWith(assertion)
                .verifyComplete();
    }

    private void expectNoResponse(CartRequest request){
        resFlux
                .doFirst(() -> this.streamBridge.send("payment-request", request))
                .timeout(Duration.ofSeconds(2), Mono.empty())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Consumer<Flux<CartPaymentResponse>> responseConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }

    }

}
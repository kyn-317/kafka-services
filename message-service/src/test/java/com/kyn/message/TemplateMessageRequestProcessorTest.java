package com.kyn.message;

import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import com.kyn.common.messages.message.TemplateMessageRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kyn.message.application.service.interfaces.MessageHistoryService;
import com.kyn.message.application.service.interfaces.MessageTemplateService;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.messaging.processor.TemplateMessageRequestProcessorImpl;

@TestPropertySource(properties = {
        "spring.cloud.function.definition=processor;responseConsumer",
        "spring.cloud.stream.bindings.responseConsumer-in-0.destination=message-response"
})
@ExtendWith(MockitoExtension.class)
public class TemplateMessageRequestProcessorTest extends AbstractIntegrationTest {

    private static final Sinks.Many<TemplateMessageRequest> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<TemplateMessageRequest> resFlux = resSink.asFlux().cache(0);

    @Autowired
    private StreamBridge streamBridge;

    @Mock
    private MessageTemplateService messageTemplateService;
    
    @Mock
    private MessagingService messagingService;
    
    @Mock
    private MessageHistoryService messageHistoryService;
    
    private TemplateMessageRequestProcessorImpl processor;
    
    @BeforeEach
    void setUp() {
        processor = new TemplateMessageRequestProcessorImpl(
            messageTemplateService,
            messagingService,
            messageHistoryService
        );
    }


    @Test
    void shouldProcessOrderCompletedMessage() {
        // given
        Map<String, String> requestItem = Map.of("userId", "test-user", "orderId", "order-123");
        TemplateMessageRequest.ORDER_COMPLETED request = TemplateMessageRequest.ORDER_COMPLETED.builder()
            .requestItem(requestItem)
            .userId("test-user")
            .orderId("order-123")
            .build();
        String templateMessage = "주문이 완료되었습니다.";
        
        when(messageTemplateService.setTemplateMessage(any(), anyInt()))
            .thenReturn(Mono.just(templateMessage));
        when(messagingService.push(anyString(), anyString(), anyString()))
            .thenReturn(Mono.empty());
        when(messageHistoryService.save(any()))
            .thenReturn(Mono.empty());
            
        // when
        Mono<Void> result = processor.handle(request);
        
        // then
        StepVerifier.create(result)
            .verifyComplete();
            
        verify(messageTemplateService).setTemplateMessage(requestItem, 1);
        verify(messagingService).push(templateMessage, "test-user", "ORDER_COMPLETED");
        verify(messageHistoryService).save(any());
    }
    
    @Test
    void shouldProcessOrderCancelledMessage() {
        // given
        Map<String, String> requestItem = Map.of("userId", "test-user", "orderId", "order-123");
        TemplateMessageRequest.ORDER_CANCELLED request = TemplateMessageRequest.ORDER_CANCELLED.builder()
            .requestItem(requestItem)
            .userId("test-user")
            .orderId("order-123")
            .build();
        String templateMessage = "주문이 취소되었습니다.";
        
        when(messageTemplateService.setTemplateMessage(any(), anyInt()))
            .thenReturn(Mono.just(templateMessage));
        when(messagingService.push(anyString(), anyString(), anyString()))
            .thenReturn(Mono.empty());
        when(messageHistoryService.save(any()))
            .thenReturn(Mono.empty());
            
        // when
        Mono<Void> result = processor.handle(request);
        
        // then
        StepVerifier.create(result)
            .verifyComplete();
            
        verify(messageTemplateService).setTemplateMessage(requestItem, 2);
        verify(messagingService).push(templateMessage, "test-user", "ORDER_CANCELLED");
        verify(messageHistoryService).save(any());
    }
    
    @Test
    void shouldProcessStockReceivedMessage() {
        // given
        Map<String, String> requestItem = Map.of("userId", "test-user", "productId", "product-123");
        TemplateMessageRequest.STOCK_RECEIVED request = TemplateMessageRequest.STOCK_RECEIVED.builder()
            .requestItem(requestItem)
            .userId("test-user")
            .build();
        String templateMessage = "재고가 입고되었습니다.";
        
        when(messageTemplateService.setTemplateMessage(any(), anyInt()))
            .thenReturn(Mono.just(templateMessage));
        when(messagingService.push(anyString(), anyString(), anyString()))
            .thenReturn(Mono.empty());
        when(messageHistoryService.save(any()))
            .thenReturn(Mono.empty());
            
        // when
        Mono<Void> result = processor.handle(request);
        
        // then
        StepVerifier.create(result)
            .verifyComplete();
            
        verify(messageTemplateService).setTemplateMessage(requestItem, 3);
        verify(messagingService).push(templateMessage, "test-user", "STOCK_RECEIVED");
        verify(messageHistoryService).save(any());
    }
    
    @Test
    void shouldHandleErrorInMessageProcessing() {
        // given
        Map<String, String> requestItem = Map.of("userId", "test-user", "orderId", "order-123");
        TemplateMessageRequest.ORDER_COMPLETED request = TemplateMessageRequest.ORDER_COMPLETED.builder()
            .requestItem(requestItem)
            .userId("test-user")
            .orderId("order-123")
            .build();
        
        when(messageTemplateService.setTemplateMessage(any(), anyInt()))
            .thenReturn(Mono.error(new RuntimeException("Template error")));
            
        // when
        Mono<Void> result = processor.handle(request);
        
        // then
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean   
        public Consumer<Flux<TemplateMessageRequest>> responseConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }

    }
}
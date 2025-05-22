package com.kyn.message;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.message.application.service.interfaces.MessageHistoryService;
import com.kyn.message.application.service.interfaces.MessageTemplateService;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.messaging.processor.TemplateMessageRequestProcessorImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@Slf4j
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
        log.info("Setting up test dependencies and mocks");
        processor = new TemplateMessageRequestProcessorImpl(
            messageTemplateService,
            messagingService,
            messageHistoryService
        );
        
        // 각 서비스의 mock 동작을 설정
        Mockito.when(messageTemplateService.setTemplateMessage(Mockito.anyMap(), Mockito.anyInt()))
            .thenAnswer(invocation -> {
                Map<String, String> requestItems = invocation.getArgument(0);
                Integer templateId = invocation.getArgument(1);
                log.info("Mock messageTemplateService.setTemplateMessage called with templateId: {}, requestItems: {}", templateId, requestItems);
                return Mono.just("mocked template message for template ID " + templateId);
            });
        
        Mockito.when(messagingService.push(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenAnswer(invocation -> {
                String message = invocation.getArgument(0);
                String userId = invocation.getArgument(1);
                String type = invocation.getArgument(2);
                log.info("Mock messagingService.push called with userId: {}, type: {}, message: {}", userId, type, message);
                return Mono.empty();
            });
            
        Mockito.when(messageHistoryService.save(Mockito.any()))
            .thenAnswer(invocation -> {
                log.info("Mock messageHistoryService.save called");
                return Mono.empty();
            });
        
        log.info("Test setup completed");
    }


    @Test
    void consumeOrderCompletedMessage() {
        log.info("Starting consumeOrderCompletedMessage test");
        
        // 1. 테스트 데이터 준비
        var request = TemplateMessageRequest.ORDER_COMPLETED.builder()
            .requestItem(Map.of("user_name", "test-user", "order_details", "order-123"))
            .userId("07a6eacb-144c-4450-a941-04f948e9bf36")
            .orderId("61418150-40d4-42f7-bdbd-ee68a7462d08")
            .build();
        
        log.info("Created test request: {}", request);
        
        // 2. 메시지 보내기 및 검증
        sendMessageAndVerify(request);
        
        // 3. 정상 처리 여부 검증
        log.info("Verifying service method calls");
        Mockito.verify(messageTemplateService, Mockito.times(1))
            .setTemplateMessage(request.requestItem(), 1);
        
        Mockito.verify(messagingService, Mockito.times(1))
            .push(Mockito.anyString(), Mockito.eq(request.userId()), Mockito.eq("ORDER_COMPLETED"));
            
        Mockito.verify(messageHistoryService, Mockito.times(1))
            .save(Mockito.any());
        
        log.info("Test completed successfully");
    }
    
    private <T extends TemplateMessageRequest> void sendMessageAndVerify(T request) {
        log.info("Processing message through processor");
        
        // Processor를 통해 메시지 처리
        StepVerifier.create(processor.process(request))
            .verifyComplete();
        
        log.info("Message processed successfully through processor");
        
        // Kafka에 메시지 보내기 시뮬레이션
        log.info("Sending message to Kafka topic: template-message-request");
        boolean sent = streamBridge.send("template-message-request", request);
        log.info("Message sent to Kafka: {}", sent);
        
        // 응답 메시지 검증 (필요한 경우)
        log.info("Verifying response flux");
        resFlux
            .timeout(Duration.ofSeconds(2), Mono.empty())
            .doOnNext(response -> log.info("Received response: {}", response))
            .as(StepVerifier::create)
            .verifyComplete();
        
        log.info("Message verification completed");
    }
  
    @TestConfiguration
    static class TestConfig {

        @Bean   
        public Consumer<Flux<TemplateMessageRequest>> responseConsumer(){
            return f -> f.doOnNext(msg -> {
                log.info("Response consumer received message: {}", msg);
                resSink.tryEmitNext(msg);
            }).subscribe();
        }

    }
}
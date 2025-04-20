package com.kyn.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.kyn.common.messages.message.MessageRequest;
import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
    
    // StreamBridge는 Spring Cloud Stream에서 자동으로 등록되지만,
    // 해당 Bean을 사용하기 위한 설정을 명시적으로 추가합니다.
    @Bean
    public Function<String, Message<MessageRequest>> messageBridge() {
        return payload -> {
            MessageRequest request = MessageRequest.Push.builder()
                    .orderId(UUID.randomUUID())
                    .userId(UUID.randomUUID())
                    .message("테스트 메시지: " + payload)
                    .build();
            
            return MessageBuilder.withPayload(request)
                    .setHeader("contentType", "application/json")
                    .build();
        };
    }
}

package com.kyn.message.integration;

import java.util.UUID;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.common.dto.MessageDto;
import com.kyn.message.common.dto.PushRequest;
import com.kyn.message.messaging.mapper.MessageDtoMapper;
import com.kyn.message.messaging.processor.MessageRequestProcessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import reactor.core.publisher.Mono;

/* @SpringBootTest
@Import(TestChannelBinderConfiguration.class) */
public class OrderMessageConsumerTest {
/* 
    @Autowired
    private InputDestination inputDestination;

    @SpyBean
    private MessageRequestProcessor messageRequestProcessor;

    @MockBean
    private MessagingService messagingService;

    private static final String MESSAGE_REQUEST_DESTINATION = "message-request";

    @Test
    public void testConsumeOrderServiceMessage() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String messageText = "주문이 접수되었습니다. 주문번호: " + orderId;
        
        MessageRequest.Push pushRequest = MessageRequest.Push.builder()
                .orderId(orderId)
                .userId(userId)
                .message(messageText)
                .build();
        
        Message<MessageRequest.Push> message = MessageBuilder.withPayload(pushRequest)
                .setHeader(KafkaHeaders.KEY, orderId.toString())
                .build();
        
        PushRequest dto = MessageDtoMapper.toPushRequest(pushRequest);
        
        // MessageDto mocking
        MessageDto mockMessageDto = MessageDto.builder()
                .messageId(UUID.randomUUID())
                .orderId(orderId)
                .userId(userId)
                .message(messageText)
                .build();
        
        when(messagingService.push(any(PushRequest.class))).thenReturn(Mono.just(mockMessageDto));
        
        // When
        inputDestination.send(message, MESSAGE_REQUEST_DESTINATION);
        
        // Then
        verify(messageRequestProcessor, times(1)).process(any(MessageRequest.class));
        verify(messagingService, times(1)).push(any(PushRequest.class));
    } */
} 
package com.kyn.order.application.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.order.common.service.MessageEventListener;
import com.kyn.order.common.service.TemplateMessageEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/message")
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageEventListener messageEventListener;
    private final TemplateMessageEventListener templateMessageEventListener;

    @PostMapping
    public Mono<MessageRequest> createMessage(@RequestBody MessageRequest.Push request) {
        log.info("Message Received: {}", request);
        messageEventListener.createMessage(request);
        return Mono.just(request);
    }

    @PostMapping("/byCartRequest")
    public Mono<TemplateMessageRequest> createMessageByCartRequest(@RequestBody TemplateMessageRequest.ORDER_COMPLETED request) {
        log.info("Message Received: {}", request);
      /*   messageEventListener.createMessage(request); */
        return Mono.just(request);
    }
}

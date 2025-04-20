package com.kyn.order.application.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.order.message.service.MessagePublishService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order/messages")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessagePublishService messagePublishService;
    
    @PostMapping("/{orderId}/{userId}")
    public Mono<Void> sendOrderMessage(
            @PathVariable UUID orderId,
            @PathVariable UUID userId,
            @RequestBody MessageRequest request) {
        return messagePublishService.publishOrderMessage(orderId, userId, request.message());
    }
    
    public record MessageRequest(String message) {}
} 
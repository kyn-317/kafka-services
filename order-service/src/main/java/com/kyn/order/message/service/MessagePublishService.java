package com.kyn.order.message.service;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface MessagePublishService {
    
    Mono<Void> publishOrderMessage(UUID orderId, UUID userId, String message);
    
} 
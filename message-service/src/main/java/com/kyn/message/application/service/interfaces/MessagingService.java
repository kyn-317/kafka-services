package com.kyn.message.application.service.interfaces;

import java.util.UUID;

import com.kyn.common.messages.message.MessageRequest;

import reactor.core.publisher.Mono;

public interface MessagingService {
    Mono<Void> push(MessageRequest.Push request);

    Mono<Void> push(String message, String userId, String type);
}

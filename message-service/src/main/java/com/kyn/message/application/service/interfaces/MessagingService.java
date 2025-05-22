package com.kyn.message.application.service.interfaces;

import reactor.core.publisher.Mono;

public interface MessagingService {
    Mono<Void> push(String message, String userId, String type);
}

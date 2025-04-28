package com.kyn.message.application.service.interfaces;

import com.kyn.common.messages.message.MessageRequest;

import reactor.core.publisher.Mono;

public interface MessagingService {
    Mono<Void> push(MessageRequest.Push request);
}

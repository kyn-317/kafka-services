package com.kyn.message.application.service.interfaces;

import com.kyn.message.application.dto.MessageHistoryRequest;
import com.kyn.message.application.dto.MessageHistoryResponse;

import reactor.core.publisher.Mono;

public interface MessageHistoryService {

    Mono<MessageHistoryResponse> save(MessageHistoryRequest messageHistoryRequest);
    
}

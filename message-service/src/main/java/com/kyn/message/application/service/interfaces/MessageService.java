package com.kyn.message.application.service.interfaces;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.Message;

import com.kyn.message.application.dto.ServerSentMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService {
    
    void sendMessage(Message<String> message);
    
    void processRequest(ServerSentMessage request);
    
    Mono<Void> sendEventToClient(String clientId, ServerSentMessage message);
    
    Flux<ServerSentEvent<String>> getEventStream();
    
    Flux<ServerSentEvent<String>> getEventStreamForClient(String clientId);
    
    void removeClient(String clientId);
    
}

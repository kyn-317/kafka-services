package com.kyn.message.application.service.interfaces;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.Message;

import com.kyn.common.messages.message.MessageRequest;

import reactor.core.publisher.Flux;

public interface MessageService {
    
    void sendMessage(Message<String> message);
    
    void processRequest(MessageRequest request);
    
    void sendEventToClient(String clientId, MessageRequest request);
    
    Flux<ServerSentEvent<String>> getEventStream();
    
    Flux<ServerSentEvent<String>> getEventStreamForClient(String clientId);
    
    void removeClient(String clientId);
    
}

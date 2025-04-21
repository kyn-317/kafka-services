package com.kyn.message.application.service;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.message.application.dto.ServerSentMessage;
import com.kyn.message.application.service.interfaces.MessageService;
import com.kyn.message.messaging.processor.MessageRequestProcessor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService{

    // sink for sending events to multiple clients
    private final Many<ServerSentEvent<String>> sink;
    
    // map for managing sinks by specific ID
    private final ConcurrentHashMap<String, Many<ServerSentEvent<String>>> sinkMap;

    public MessageServiceImpl() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.sinkMap = new ConcurrentHashMap<>();
    }

    @Override
    public void sendMessage(Message<String> message) {
        log.info("message received: {}", message.getPayload());
        
        // send message to all subscribers
        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event("message")
                .data(message.getPayload())
                .build();
                
        sink.tryEmitNext(event);
    }
    
    /**
     * Request object to ServerSentEvent
     */
    @Override
    public void processRequest(ServerSentMessage message) {
        log.info("request processing: {}", message);
        
        // determine event type based on Request type
        String eventType = message.getClass().getSimpleName();
        
        // create event
        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event(eventType)
                .data(message.toString())
                .build();
                
        // publish event to all subscribers
        sink.tryEmitNext(event);
    }
    
    /**
     * return SSE stream for specific client
     */
    @Override
    public Mono<Void> sendEventToClient(String clientId, ServerSentMessage message) {
        log.info("client {} sending event: {}", clientId, message);
        
        Many<ServerSentEvent<String>> clientSink = sinkMap.computeIfAbsent(
            clientId, id -> Sinks.many().multicast().onBackpressureBuffer()
        );
        
        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event(message.getClass().getSimpleName())
                .data(message.toString())
                .build();
                
        clientSink.tryEmitNext(event);
        return Mono.empty();
    }
    
    /**
     * return SSE stream for all clients
     */
    @Override
    public Flux<ServerSentEvent<String>> getEventStream() {
        return sink.asFlux()
                .mergeWith(getHeartbeat());
    }
    
    /**
     * return SSE stream for specific client
     */
    @Override
    public Flux<ServerSentEvent<String>> getEventStreamForClient(String clientId) {
        Many<ServerSentEvent<String>> clientSink = sinkMap.computeIfAbsent(
            clientId, id -> Sinks.many().multicast().onBackpressureBuffer()
        );
        
        return clientSink.asFlux()
                .mergeWith(getHeartbeat());
    }
    
    /**
     * keepalive event for connection maintenance
     */
    private Flux<ServerSentEvent<String>> getHeartbeat() {
        return Flux.interval(Duration.ofSeconds(15))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("heartbeat")
                        .data("ping")
                        .build());
    }
    
    /**
     * disconnect client
     */
    @Override
    public void removeClient(String clientId) {
        sinkMap.remove(clientId);
        log.info("client {} disconnected", clientId);
    }

}

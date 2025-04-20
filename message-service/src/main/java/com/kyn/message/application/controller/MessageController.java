package com.kyn.message.application.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.message.MessageRequest;
import com.kyn.message.application.dto.MessageData;
import com.kyn.message.application.dto.ServerSentMessage;
import com.kyn.message.application.service.MessageServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageServiceImpl messageService;

    /**
     * Provide SSE stream for all clients
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        log.info("Client start streaming");
        return messageService.getEventStream();
    }

    /**
     * Provide SSE stream for specific client
     */
    @GetMapping(value = "/stream/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEventsForClient(@PathVariable String clientId) {
        log.info("Client ID: {} start private event stream", clientId);
        return messageService.getEventStreamForClient(clientId);
    }

    /**
     * Send event to all clients
     */
    @PostMapping("/publish")
    public Mono<Void> publishEvent(@RequestBody MessageRequest request) {
        log.info("Event publish request: {}", request);
        var serverSentMessage = ServerSentMessage.builder()
            .type("push")
            .data(MessageData.builder()
                .message(request)
                .build())
            .build();
        messageService.processRequest(serverSentMessage);
        return Mono.empty();
    }

    /**
     * Send event to specific client
     */
    @PostMapping("/publish/{clientId}")
    public Mono<Void> publishEventToClient(
            @PathVariable String clientId,
            @RequestBody MessageRequest request) {
        log.info("Client {} publish event request: {}", clientId, request);
        var serverSentMessage = ServerSentMessage.builder()
            .type("push")
            .data(MessageData.builder()
                .message(request)
                .build())
            .build();
        messageService.sendEventToClient(clientId, serverSentMessage);
        return Mono.empty();
    }

    /**
     * Disconnect client
     */
    @DeleteMapping("/clients/{clientId}")
    public Mono<Void> removeClient(@PathVariable String clientId) {
        log.info("Client {} disconnect request", clientId);
        messageService.removeClient(clientId);
        return Mono.empty();
    }
} 
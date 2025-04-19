package com.kyn.message.application.service;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
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

    // 여러 클라이언트에게 이벤트를 전송하기 위한 Sink
    private final Many<ServerSentEvent<String>> sink;
    
    // 특정 ID별로 Sink를 관리하기 위한 맵
    private final ConcurrentHashMap<String, Many<ServerSentEvent<String>>> sinkMap;

    public MessageServiceImpl() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.sinkMap = new ConcurrentHashMap<>();
    }

    @Override
    public void sendMessage(Message<String> message) {
        log.info("메시지 수신: {}", message.getPayload());
        
        // 모든 구독자에게 메시지 전송
        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event("message")
                .data(message.getPayload())
                .build();
                
        sink.tryEmitNext(event);
    }
    
    /**
     * Request 객체를 받아서 ServerSentEvent로 변환하여 발행
     */
    public void processRequest(MessageRequest request) {
        log.info("요청 처리: {}", request);
        
        // Request 타입에 따라 이벤트 타입 결정
        String eventType = request.getClass().getSimpleName();
        
        // 이벤트 생성
        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event(eventType)
                .data(request.toString())
                .build();
                
        // 모든 구독자에게 이벤트 발행
        sink.tryEmitNext(event);
    }
    
    /**
     * 클라이언트별 ID를 받아 해당 클라이언트에게만 이벤트 전송
     */
    public void sendEventToClient(String clientId, MessageRequest request) {
        log.info("클라이언트 {} 에게 이벤트 전송: {}", clientId, request);
        
        Many<ServerSentEvent<String>> clientSink = sinkMap.computeIfAbsent(
            clientId, id -> Sinks.many().multicast().onBackpressureBuffer()
        );
        
        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event(request.getClass().getSimpleName())
                .data(request.toString())
                .build();
                
        clientSink.tryEmitNext(event);
    }
    
    /**
     * 모든 클라이언트를 위한 SSE 스트림 반환
     */
    public Flux<ServerSentEvent<String>> getEventStream() {
        return sink.asFlux()
                .mergeWith(getHeartbeat());
    }
    
    /**
     * 특정 클라이언트를 위한 SSE 스트림 반환
     */
    public Flux<ServerSentEvent<String>> getEventStreamForClient(String clientId) {
        Many<ServerSentEvent<String>> clientSink = sinkMap.computeIfAbsent(
            clientId, id -> Sinks.many().multicast().onBackpressureBuffer()
        );
        
        return clientSink.asFlux()
                .mergeWith(getHeartbeat());
    }
    
    /**
     * 연결 유지를 위한 하트비트(keepalive) 이벤트 생성
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
     * 클라이언트 연결 해제 시 리소스 정리
     */
    public void removeClient(String clientId) {
        sinkMap.remove(clientId);
        log.info("클라이언트 {} 연결 해제됨", clientId);
    }

}

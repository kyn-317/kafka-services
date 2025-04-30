package com.kyn.order.message.publisher;

import java.time.Duration;
import java.util.EventListener;
import java.util.UUID;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.CartEventListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
public class CartEventListenerImpl implements CartEventListener, EventPublisher<UUID> {
    
        private final Sinks.Many<UUID> sink;
        private final Flux<UUID> flux;
    
        @Override
        public Flux<UUID> publish() {
            return this.flux;
        }
    
        @Override
        public void emitOrderCreated(OrderSummaryDto dto) {
            this.sink.emitNext(
                    dto.getOrderId(),
                    Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
            );
        }
    
    }
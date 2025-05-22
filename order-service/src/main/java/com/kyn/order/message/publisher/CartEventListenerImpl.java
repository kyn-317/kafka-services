package com.kyn.order.message.publisher;

import java.time.Duration;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.service.CartEventListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
public class CartEventListenerImpl implements CartEventListener, EventPublisher<OrderSummaryDto> {
    
        private final Sinks.Many<OrderSummaryDto> sink;
        private final Flux<OrderSummaryDto> flux;
    
        @Override
        public Flux<OrderSummaryDto> publish() {
            return this.flux;
        }
    
        @Override
        public void emitOrderCreated(OrderSummaryDto dto) {
            this.sink.emitNext(
                    dto,
                    Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
            );
        }
    
    }
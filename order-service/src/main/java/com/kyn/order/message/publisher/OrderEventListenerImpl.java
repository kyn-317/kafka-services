package com.kyn.order.message.publisher;

import java.time.Duration;
import java.util.UUID;

import com.kyn.common.publisher.EventPublisher;
import com.kyn.order.common.dto.PurchaseOrderDto;
import com.kyn.order.common.service.OrderEventListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener, EventPublisher<UUID> {

    private final Sinks.Many<UUID> sink;
    private final Flux<UUID> flux;

    @Override
    public Flux<UUID> publish() {
        return this.flux;
    }

    @Override
    public void emitOrderCreated(PurchaseOrderDto dto) {
        this.sink.emitNext(
                dto.orderId(),
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }

}

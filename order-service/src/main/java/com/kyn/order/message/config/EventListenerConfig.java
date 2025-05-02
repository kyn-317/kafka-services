package com.kyn.order.message.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.message.MessageRequest;
import com.kyn.order.common.service.CartEventListener;
import com.kyn.order.common.service.MessageEventListener;
import com.kyn.order.common.service.OrderEventListener;
import com.kyn.order.message.publisher.CartEventListenerImpl;
import com.kyn.order.message.publisher.MessageEventListenerImpl;
import com.kyn.order.message.publisher.OrderEventListenerImpl;

import reactor.core.publisher.Sinks;
@Configuration
public class EventListenerConfig {

    @Bean
    public OrderEventListener orderEventListener(){
        var sink = Sinks.many().unicast().<UUID>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new OrderEventListenerImpl(sink, flux);
    }

    @Bean
    public MessageEventListener messageEventListener(){
        var sink = Sinks.many().unicast().<MessageRequest>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new MessageEventListenerImpl(sink, flux);
    }

    @Bean
    public CartEventListener cartEventListener(){
        var sink = Sinks.many().unicast().<OrderSummaryDto>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new CartEventListenerImpl(sink, flux);
    }
}


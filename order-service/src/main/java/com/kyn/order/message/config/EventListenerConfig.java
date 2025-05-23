package com.kyn.order.message.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.message.TemplateMessageRequest;
import com.kyn.order.common.service.CartEventListener;
import com.kyn.order.common.service.OrderEventListener;
import com.kyn.order.common.service.TemplateMessageEventListener;
import com.kyn.order.message.publisher.CartEventListenerImpl;

import com.kyn.order.message.publisher.OrderEventListenerImpl;
import com.kyn.order.message.publisher.TemplateMessageEventListenerImpl;

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
    public CartEventListener cartEventListener(){
        var sink = Sinks.many().unicast().<OrderSummaryDto>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new CartEventListenerImpl(sink, flux);
    }

    @Bean
    public TemplateMessageEventListener templateMessageEventListener(){
        var sink = Sinks.many().unicast().<TemplateMessageRequest>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new TemplateMessageEventListenerImpl(sink, flux);
    }
}
package com.kyn.inventory.messaging.processor;

import java.util.function.UnaryOperator;

import org.springframework.stereotype.Service;

import com.kyn.common.exception.EventAlreadyProcessedException;
import com.kyn.common.messages.inventory.CartInventoryRequest.Deduct;
import com.kyn.common.messages.inventory.CartInventoryRequest.Restore;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.inventory.common.mapper.MessageDtoMapper;
import com.kyn.inventory.common.service.CartInventoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryRequestProcessorImpl implements InventoryRequestProcessor {
    
    private final CartInventoryService service;
    
    @Override
    public Mono<CartInventoryResponse> handle(Deduct request) {
        return this.service.deduct(request)
                .transform(this.exceptionHandler(request));
    }

    @Override
    public Mono<CartInventoryResponse> handle(Restore request) {
        return this.service.restore(request)
                .then(Mono.empty());
    }
    
    private UnaryOperator<Mono<CartInventoryResponse>> exceptionHandler(Deduct request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, e -> Mono.empty())
                           .onErrorResume(MessageDtoMapper.toInventoryDeclinedResponse(request));
    }
}

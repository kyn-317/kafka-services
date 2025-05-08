package com.kyn.inventory.messaging.processor;

import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.common.processor.CartRequestProcessor;

import reactor.core.publisher.Mono;

public interface InventoryRequestProcessor extends CartRequestProcessor<CartInventoryRequest, CartInventoryResponse>{
    
    @Override
    default Mono<CartInventoryResponse> process(CartInventoryRequest request) {
        return switch (request){
            case CartInventoryRequest.Deduct p -> this.handle(p);
            case CartInventoryRequest.Restore p -> this.handle(p);
        };
    }

    Mono<CartInventoryResponse> handle(CartInventoryRequest.Deduct request);

    Mono<CartInventoryResponse> handle(CartInventoryRequest.Restore request);

}

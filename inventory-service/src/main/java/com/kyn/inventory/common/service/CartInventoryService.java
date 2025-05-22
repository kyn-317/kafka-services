package com.kyn.inventory.common.service;

import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;

import reactor.core.publisher.Mono;

public interface CartInventoryService {
    
    Mono<CartInventoryResponse> deduct(CartInventoryRequest.Deduct request);

    Mono<CartInventoryResponse> restore(CartInventoryRequest.Restore request);
}

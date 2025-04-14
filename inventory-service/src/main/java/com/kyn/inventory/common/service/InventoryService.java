package com.kyn.inventory.common.service;

import com.kyn.inventory.common.dto.InventoryDeductRequest;
import com.kyn.inventory.common.dto.OrderInventoryDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InventoryService {

    Mono<OrderInventoryDto> deduct(InventoryDeductRequest request);

    Mono<OrderInventoryDto> restore(UUID orderId);

}

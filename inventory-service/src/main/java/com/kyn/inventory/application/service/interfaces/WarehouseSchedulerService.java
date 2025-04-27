package com.kyn.inventory.application.service.interfaces;

import reactor.core.publisher.Mono;

public interface WarehouseSchedulerService {
    
    Mono<Void> processWarehouseDailyBatch();

    
    
}

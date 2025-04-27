package com.kyn.inventory.application.service.interfaces;


import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseRequestDto;
import com.kyn.inventory.application.dto.WarehouseSearch;
import com.kyn.inventory.application.entity.Warehouse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WarehouseService {

    Mono<Warehouse> deduct(WarehouseRequestDto request);

    Mono<Warehouse> restore(WarehouseRequestDto request);

    Mono<CurrentStock> findCurrentStockWithDetails(WarehouseSearch request);

    Flux<CurrentStock> findDailyStockSummary();
    
    Flux<WarehouseDto> findByProductIdAndSnapshotDate(WarehouseSearch request);
    
    Flux<WarehouseDto> findBySnapshotDate(WarehouseSearch request);
    
    Mono<Void> deleteBySnapshotDate(WarehouseSearch request);

    Flux<WarehouseDto> findByConditions(WarehouseSearch request);
}

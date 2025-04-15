package com.kyn.inventory.application.service.interfaces;

import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseHistoryRequestDto;

import reactor.core.publisher.Flux;

public interface WarehouseHistoryService {

    Flux<WarehouseDto> findHistory(WarehouseHistoryRequestDto request);
    
}

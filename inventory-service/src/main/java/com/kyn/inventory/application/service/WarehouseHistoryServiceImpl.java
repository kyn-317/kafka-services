package com.kyn.inventory.application.service;

import org.springframework.stereotype.Service;

import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseHistoryRequestDto;
import com.kyn.inventory.application.mapper.EntityDtoMapper;
import com.kyn.inventory.application.repository.WarehouseHistoryRepository;
import com.kyn.inventory.application.service.interfaces.WarehouseHistoryService;

import reactor.core.publisher.Flux;

@Service
public class WarehouseHistoryServiceImpl implements WarehouseHistoryService {

    private final WarehouseHistoryRepository warehouseHistoryRepository;
    public WarehouseHistoryServiceImpl(WarehouseHistoryRepository warehouseHistoryRepository) {
        this.warehouseHistoryRepository = warehouseHistoryRepository;
    }
    
    @Override
    public Flux<WarehouseDto> findHistory(WarehouseHistoryRequestDto request) {

        return warehouseHistoryRepository.findByConditions(
            request.productId(),
            request.retrievalType(),
            request.fromDate(),
            request.toDate()
        )
        .map(EntityDtoMapper::historyToWarehouseDto);
    }
}

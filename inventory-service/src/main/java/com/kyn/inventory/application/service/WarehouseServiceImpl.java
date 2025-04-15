package com.kyn.inventory.application.service;

import org.springframework.stereotype.Service;

import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseRequestDto;
import com.kyn.inventory.application.dto.WarehouseSearch;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.mapper.EntityDtoMapper;
import com.kyn.inventory.application.repository.WarehouseRepository;
import com.kyn.inventory.application.service.interfaces.WarehouseService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Mono<Warehouse> deduct(WarehouseRequestDto request) {
        return Mono.empty();
    }

    @Override
    public Mono<Warehouse> restore(WarehouseRequestDto request) {
        return Mono.empty();
    }

    @Override
    public Flux<CurrentStock> findCurrentStockWithDetails(WarehouseSearch request) {
        return warehouseRepository.findCurrentStockWithDetails(request.productId(), request.snapshotDate());
    }

    @Override
    public Flux<CurrentStock> findDailyStockSummary(WarehouseSearch request) {
        return warehouseRepository.findDailyStockSummary(request.snapshotDate());
    }

    @Override
    public Flux<WarehouseDto> findByProductIdAndSnapshotDate(WarehouseSearch request) {
        return warehouseRepository.findByProductIdAndSnapshotDate(request.productId(), request.snapshotDate())
        .map(EntityDtoMapper::toWarehouseDto);
    }

    @Override
    public Flux<WarehouseDto> findBySnapshotDate(WarehouseSearch request) {
        return warehouseRepository.findBySnapshotDate(request.snapshotDate())
        .map(EntityDtoMapper::toWarehouseDto);
    }

    @Override
    public Mono<Void> deleteBySnapshotDate(WarehouseSearch request) {
        return warehouseRepository.deleteBySnapshotDate(request.snapshotDate());
    }

    @Override
    public Flux<WarehouseDto> findByConditions(WarehouseSearch request) {
        return warehouseRepository.findByConditions(
            request.productId(),
            request.retrievalType(),
            request.fromDate(),
            request.toDate()
        )
        .map(EntityDtoMapper::toWarehouseDto);
    }
}
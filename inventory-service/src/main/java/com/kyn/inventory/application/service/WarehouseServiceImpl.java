package com.kyn.inventory.application.service;

import java.time.LocalDateTime;

import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Service;

import com.kyn.common.util.DuplicateEventValidator;
import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseRequestDto;
import com.kyn.inventory.application.dto.WarehouseSearch;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.mapper.EntityDtoMapper;
import com.kyn.inventory.application.repository.WarehouseRepository;
import com.kyn.inventory.application.service.interfaces.WarehouseService;
import com.kyn.inventory.application.util.FormatUtil;

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
        return 
        /* DuplicateEventValidator.validate(
            this.warehouseRepository.existsByOrderId(request.orderId()),
        )
         */
        warehouseRepository.findCurrentStockWithDetails(request.productId())
        .flatMap(currentStock -> {
            if (currentStock.currentStock() < request.quantity()) {
                return Mono.error(new RuntimeException("Insufficient stock"));
            }
            return save(request);
        });
    }
    

    @Override
    public Mono<Warehouse> restore(WarehouseRequestDto request) {
        return save(request);
    }

    private Mono<Warehouse> save(WarehouseRequestDto request) {
        var warehouse = Warehouse.builder()
        .productId(request.productId())
        .requesterId(request.requesterId())
        .orderId(request.orderId())
        .retrievalType(request.retrievalType())
        .quantity(request.quantity())
        .snapshotDate(LocalDateTime.now().format(FormatUtil.DATE_FORMATTER))
        .build();
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Mono<CurrentStock> findCurrentStockWithDetails(WarehouseSearch request) {
        return warehouseRepository.findCurrentStockWithDetails(request.productId());
    }

    @Override
    public Flux<CurrentStock> findDailyStockSummary() {
        return warehouseRepository.findDailyStockSummary();
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
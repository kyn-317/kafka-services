package com.kyn.inventory.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kyn.common.util.DuplicateEventValidator;
import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseRequestDto;
import com.kyn.inventory.application.dto.WarehouseSearch;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.enums.StorageRetrievalType;
import com.kyn.inventory.application.mapper.EntityDtoMapper;
import com.kyn.inventory.application.repository.WarehouseRepository;
import com.kyn.inventory.application.service.interfaces.WarehouseService;
import com.kyn.inventory.application.util.FormatUtil;
import com.kyn.inventory.common.exception.OutOfStockException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private static final Mono<CurrentStock> OUT_OF_STOCK = Mono.error(new OutOfStockException());
    private final WarehouseRepository warehouseRepository;
    @Override
    public Mono<Warehouse> deduct(WarehouseRequestDto request) {
        return DuplicateEventValidator.validate(
            this.warehouseRepository.existsByOrderIdAndRetrievalTypeAndProductId(request.orderId(), request.retrievalType().toString(), request.productId()),
            this.warehouseRepository.findCurrentStockWithDetails(request.productId())
            )
            .filter(currentStock -> currentStock.currentStock() >= request.quantity())
            .switchIfEmpty(OUT_OF_STOCK)
            .flatMap(currentStock -> save(request))
            .doOnNext(onNext -> log.info("deduct call {}", onNext));
    }
    

    @Override
    public Mono<Warehouse> restore(WarehouseRequestDto request) {
        return DuplicateEventValidator.validate(
            this.warehouseRepository.existsByOrderIdAndRetrievalTypeAndProductId(request.orderId(), request.retrievalType().toString(), request.productId()),
            warehouseRepository.findByOrderIdAndProductIdAndRetrievalType(
                request.orderId(), 
                request.productId(), 
                getRetrievalTypeWhenRestore(request)
            )
            .filter(warehouse -> warehouse.getQuantity() == request.quantity())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("No matching base retrieval record found")))
            .flatMap(warehouse -> save(request)));
    }

    private String getRetrievalTypeWhenRestore(WarehouseRequestDto request) {
        return switch (request.retrievalType()) {
            case RECEIVING -> StorageRetrievalType.RECEIVING_CANCEL.toString();
            case RETRIEVAL -> StorageRetrievalType.RETRIEVAL_CANCEL.toString();
            default -> throw new IllegalArgumentException("Invalid retrieval type");
        };
    }

    private Mono<Warehouse> save(WarehouseRequestDto request) {
        var warehouse = Warehouse.builder()
        .productId(request.productId())
        .requesterId(request.requesterId())
        .orderId(request.orderId())
        .storageRetrievalType(request.retrievalType().toString())
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
    public Flux<CurrentStock> findDailyStockSummary(String snapshotDate) {
        return warehouseRepository.findDailyStockSummary(snapshotDate);
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
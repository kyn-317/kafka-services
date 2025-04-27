package com.kyn.inventory.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseSearch;
import com.kyn.inventory.application.repository.WarehouseRepository;
import com.kyn.inventory.application.service.interfaces.RedissonStockService;
import com.kyn.inventory.application.service.interfaces.WarehouseService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RedissonStockServiceImpl implements RedissonStockService {
    
    private final RMapReactive<String, Integer> stockMap;
    private final WarehouseService warehouseService;
    private final WarehouseRepository warehouseRepository;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    public RedissonStockServiceImpl(RedissonClient redissonClient, 
                                   WarehouseService warehouseService,
                                   WarehouseRepository warehouseRepository) {
        this.stockMap = redissonClient.reactive().getMap("stock");
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Mono<Integer> getStock(String productId) {
        return stockMap.get(productId)
        .switchIfEmpty(warehouseService.findCurrentStockWithDetails(
            new WarehouseSearch(UUID.fromString(productId), LocalDate.now().format(DATE_FORMATTER), null, null, null))
            .map(CurrentStock::currentStock)
            .flatMap(stock -> insertStock(productId, stock))
        );
     }

    @Override
    public Mono<Integer> insertStock(String productId, Integer stock) {
        return this.stockMap.put(productId, stock)
        .thenReturn(stock);
    }

    @Override
    public Mono<Integer> deductStock(String productId, Integer stock) {
        BiFunction<String, Integer, Integer> deductIfSufficient = (key, currentStock) -> {
            return (currentStock == null)? -1 :(currentStock < stock) ? -2 : currentStock - stock;
        };
        return stockMap.compute(productId, deductIfSufficient);
    }
    
    @Override
    public Mono<Map<String, Integer>> getStockBatch(Iterable<String> productIds) {
        return Flux.fromIterable(productIds)
            .flatMap(productId -> getStock(productId)
                .map(stock -> Map.entry(productId, stock)))
            .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }
    
    @Override
    public Mono<Void> insertStockBatch(Map<String, Integer> stocks) {
        return Flux.fromIterable(stocks.entrySet())
            .flatMap(entry -> stockMap.fastPut(entry.getKey(), entry.getValue()))
            .then();
    }
    
    @Override
    public Mono<Void> refreshCache(String productId) {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return warehouseService.findCurrentStockWithDetails(
            new WarehouseSearch(UUID.fromString(productId), today, null, null, null))
            .map(CurrentStock::currentStock)
            .flatMap(stock -> stockMap.fastPut(productId, stock))
            .doOnSuccess(res -> log.info("Refreshed cache for product {}", productId))
            .then();
    }
    
    @Override
    public Flux<String> refreshAllCache() {
        return warehouseRepository.findDailyStockSummary()
            .flatMap(stock -> {
                String productIdStr = stock.productId().toString();
                return stockMap.fastPut(productIdStr, stock.currentStock())
                    .thenReturn(productIdStr);
            })
            .doOnComplete(() -> log.info("Refreshed all product caches"));
    }
}
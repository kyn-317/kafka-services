package com.kyn.inventory.application.service.interfaces;

import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RedissonStockService {
    /**
     * Get current stock of a product.
     * If not in Redis, fetch from DB and store in cache.
     */
    Mono<Integer> getStock(String productId);
    
    /**
     * Store product stock in Redis.
     */
    Mono<Integer> insertStock(String productId, Integer stock);
    
    /**
     * Deduct product stock.
     * Returns -2 if insufficient stock.
     * Returns -1 if stock info not found.
     */
    Mono<Integer> deductStock(String productId, Integer stock);
    
    /**
     * Get stock for multiple products at once.
     */
    Mono<Map<String, Integer>> getStockBatch(Iterable<String> productIds);
    
    /**
     * Store stock for multiple products at once.
     */
    Mono<Void> insertStockBatch(Map<String, Integer> stocks);
    
    /**
     * Refresh Redis cache stock data with latest data from DB.
     */
    Mono<Void> refreshCache(String productId);
    
    /**
     * Refresh cache for all products.
     */
    Flux<String> refreshAllCache(boolean isDaily);
}

package com.kyn.inventory.application.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.kyn.inventory.application.entity.WarehouseHistory;
import com.kyn.inventory.application.enums.StorageRetrievalType;

import reactor.core.publisher.Flux;

public interface WarehouseHistoryRepository extends ReactiveCrudRepository<WarehouseHistory, UUID> {
    
    Flux<WarehouseHistory> findBySnapshotDateBetweenOrderByCreatedByAsc(String startDate, String endDate);
    
    @Query("""
        SELECT * FROM warehouse_history 
        WHERE (:productId IS NULL OR product_id = :productId)
        AND (:retrievalType IS NULL OR storage_retrieval_type = :retrievalType)
        AND snapshot_date BETWEEN :startDate AND :endDate
        ORDER BY created_by ASC
    """)
    Flux<WarehouseHistory> findByConditions(
        UUID productId,
        StorageRetrievalType retrievalType,
        String startDate,
        String endDate
    );
} 
package com.kyn.inventory.application.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.enums.StorageRetrievalType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WarehouseRepository extends ReactiveCrudRepository<Warehouse, UUID> {
    
    @Query("""
        WITH current_stock AS (
            SELECT 
                product_id,
                snapshot_date,
                SUM(CASE 
                    WHEN storage_retrieval_type = 'BASE' THEN amount
                    WHEN storage_retrieval_type = 'RECEIVING' THEN amount
                    WHEN storage_retrieval_type = 'RETRIEVAL' THEN -amount
                    WHEN storage_retrieval_type = 'RECEIVING_CANCEL' THEN -amount
                    WHEN storage_retrieval_type = 'RETRIEVAL_CANCEL' THEN amount
                END) as current_stock
            FROM warehouse
            WHERE product_id = :productId
            AND storage_retrieval_type IN ('BASE', 'RECEIVING', 'RETRIEVAL', 'RECEIVING_CANCEL', 'RETRIEVAL_CANCEL')
            GROUP BY product_id, snapshot_date
        )
        SELECT 
            w.*,
            cs.current_stock
        FROM warehouse w
        JOIN current_stock cs ON w.product_id = cs.product_id AND w.snapshot_date = cs.snapshot_date
        WHERE w.product_id = :productId
    """)
    Mono<CurrentStock> findCurrentStockWithDetails(UUID productId);
    @Query("""
        SELECT 
            product_id,
            snapshot_date,
            SUM(CASE 
                WHEN storage_retrieval_type = 'BASE' THEN amount
                WHEN storage_retrieval_type = 'RECEIVING' THEN amount
                WHEN storage_retrieval_type = 'RETRIEVAL' THEN -amount
                WHEN storage_retrieval_type = 'RECEIVING_CANCEL' THEN -amount
                WHEN storage_retrieval_type = 'RETRIEVAL_CANCEL' THEN amount
            END) as current_stock
        FROM warehouse
        WHERE storage_retrieval_type IN ('BASE', 'RECEIVING', 'RETRIEVAL')
        GROUP BY product_id, snapshot_date
        ORDER BY product_id
    """)
    Flux<CurrentStock> findDailyStockSummary();

    Flux<Warehouse> findByProductIdAndSnapshotDate(UUID productId, String snapshotDate);
    
    Flux<Warehouse> findBySnapshotDate(String snapshotDate);
    
    Mono<Void> deleteBySnapshotDate(String snapshotDate);

    @Query("""
        SELECT * FROM warehouse 
        WHERE (:productId IS NULL OR product_id = :productId)
        AND (:retrievalType IS NULL OR storage_retrieval_type = :retrievalType)
        AND snapshot_date BETWEEN :startDate AND :endDate
        ORDER BY created_by ASC
    """)
    Flux<Warehouse> findByConditions(
        UUID productId,
        StorageRetrievalType retrievalType,
        String startDate,
        String endDate
    );
}

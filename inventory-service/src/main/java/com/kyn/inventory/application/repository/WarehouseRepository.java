package com.kyn.inventory.application.repository;

import java.util.UUID;
import java.time.LocalDate;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import com.kyn.inventory.application.entity.Warehouse;

public interface WarehouseRepository extends ReactiveCrudRepository<Warehouse, UUID> {
    
    @Query("""
        WITH daily_stock AS (
            SELECT 
                product_id,
                snapshot_date,
                SUM(CASE 
                    WHEN storage_retrieval_type = 'BASE' THEN amount
                    WHEN storage_retrieval_type = 'RECEIVING' THEN amount
                    WHEN storage_retrieval_type = 'RETRIEVAL' THEN -amount
                END) as current_stock
            FROM warehouse
            WHERE product_id = :productId
            AND snapshot_date = :snapshotDate
            AND storage_retrieval_type IN ('BASE', 'RECEIVING', 'RETRIEVAL')
            GROUP BY product_id, snapshot_date
        )
        SELECT 
            w.*,
            ds.current_stock
        FROM warehouse w
        JOIN daily_stock ds ON w.product_id = ds.product_id AND w.snapshot_date = ds.snapshot_date
        WHERE w.product_id = :productId
        AND w.snapshot_date = :snapshotDate
        ORDER BY w.snapshot_date DESC
    """)
    Flux<Warehouse> findCurrentStockWithDetails(UUID productId, LocalDate snapshotDate);

    @Query("""
        SELECT 
            product_id,
            snapshot_date,
            SUM(CASE 
                WHEN storage_retrieval_type = 'BASE' THEN amount
                WHEN storage_retrieval_type = 'RECEIVING' THEN amount
                WHEN storage_retrieval_type = 'RETRIEVAL' THEN -amount
            END) as current_stock
        FROM warehouse
        WHERE snapshot_date = :date
        AND storage_retrieval_type IN ('BASE', 'RECEIVING', 'RETRIEVAL')
        GROUP BY product_id, snapshot_date
        ORDER BY product_id
    """)
    Flux<Object[]> findDailyStockSummary(LocalDate date);
}

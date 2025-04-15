package com.kyn.inventory.application.mapper;

import java.time.LocalDateTime;

import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.entity.OrderInventory;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.entity.WarehouseHistory;
import com.kyn.inventory.application.enums.StorageRetrievalType;
import com.kyn.inventory.common.dto.InventoryDeductRequest;
import com.kyn.inventory.common.dto.OrderInventoryDto;

public class EntityDtoMapper {

    public static OrderInventory toOrderInventory(InventoryDeductRequest request) {
        return OrderInventory.builder()
                             .orderId(request.orderId())
                             .productId(request.productId())
                             .quantity(request.quantity())
                             .build();
    }

    public static OrderInventoryDto toDto(OrderInventory orderInventory) {
        return OrderInventoryDto.builder()
                                .inventoryId(orderInventory.getInventoryId())
                                .orderId(orderInventory.getOrderId())
                                .productId(orderInventory.getProductId())
                                .quantity(orderInventory.getQuantity())
                                .status(orderInventory.getStatus())
                                .build();
    }


    public static WarehouseHistory toWarehouseHistory(Warehouse warehouse) {
        return WarehouseHistory.builder()
            .id(warehouse.getId())
            .productId(warehouse.getProductId())
            .requesterId(warehouse.getRequesterId())
            .orderId(warehouse.getOrderId())
            .retrievalType(warehouse.getRetrievalType())
            .quantity(warehouse.getQuantity())
            .snapshotDate(warehouse.getSnapshotDate())
            .createdAt(warehouse.getCreatedAt())
            .createdBy(warehouse.getCreatedBy())
            .build();
    }

    public static Warehouse setBaseStock(CurrentStock currentStock) {
        return Warehouse.builder()
            .productId(currentStock.productId())
            .quantity(currentStock.currentStock())
            .snapshotDate(currentStock.snapshotDate())
            .retrievalType(StorageRetrievalType.BASE)
            .createdAt(LocalDateTime.now())
            .createdBy("SYSTEM")
            .build();
    }


    public static WarehouseDto historyToWarehouseDto(WarehouseHistory warehouseHistory) {
        return new WarehouseDto(
            warehouseHistory.getId(),
            warehouseHistory.getProductId(),
            warehouseHistory.getRequesterId(),
            warehouseHistory.getOrderId(),
            warehouseHistory.getRetrievalType(),
            warehouseHistory.getQuantity(),
            warehouseHistory.getSnapshotDate(),
            warehouseHistory.getCreatedAt(),
            warehouseHistory.getCreatedBy()
        );
    }

    public static WarehouseDto toWarehouseDto(Warehouse warehouse) {
        return new WarehouseDto(
            warehouse.getId(),
            warehouse.getProductId(),
            warehouse.getRequesterId(),
            warehouse.getOrderId(),
            warehouse.getRetrievalType(),
            warehouse.getQuantity(),
            warehouse.getSnapshotDate(),
            warehouse.getCreatedAt(),
            warehouse.getCreatedBy()
        );
    }

}

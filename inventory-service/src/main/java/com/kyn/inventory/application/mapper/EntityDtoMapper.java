package com.kyn.inventory.application.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.kyn.common.dto.OrderDetailDto;
import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseDto;
import com.kyn.inventory.application.dto.WarehouseRequestDto;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.entity.WarehouseHistory;
import com.kyn.inventory.application.enums.StorageRetrievalType;

public class EntityDtoMapper {



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

    public static WarehouseRequestDto detailToRequest(OrderDetailDto detail, UUID customerId , StorageRetrievalType retrievalType) {

        return WarehouseRequestDto.builder()
            .productId(detail.getProductId())
            .requesterId(customerId)
            .orderId(detail.getOrderId())
            .retrievalType(retrievalType)
            .quantity(detail.getQuantity())
            .build();
    }

    public static CartInventoryResponse.Deducted toDeducted(OrderSummaryDto summary, List<UUID> inventoryIds) {
        return CartInventoryResponse.Deducted.builder()
            .responseItem(summary)
            .inventoryIds(inventoryIds)
            .build();
    }

    public static CartInventoryResponse.Declined toDeclined(OrderSummaryDto summary, String message) {
        return CartInventoryResponse.Declined.builder()
            .responseItem(summary)
            .message(message)
            .build();
    }
}

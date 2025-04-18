package com.kyn.inventory.application.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.dto.WarehouseSearch;

import com.kyn.inventory.application.service.interfaces.WarehouseService;
import com.kyn.inventory.application.util.FormatUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("/inventory")
public class InventoryController {
    

    private final WarehouseService warehouseService;

    public InventoryController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @MessageMapping("{productId}")
    public Mono<CurrentStock> getInventory(@DestinationVariable UUID productId) {
        return warehouseService.findCurrentStockWithDetails(
            new WarehouseSearch(productId, 
            FormatUtil.DATE_FORMATTER.format(LocalDate.now()),
            null,
            null,
            null));
    }
}

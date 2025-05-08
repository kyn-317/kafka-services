package com.kyn.inventory.application.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.inventory.application.dto.CurrentStock;
import com.kyn.inventory.application.repository.WarehouseRepository;
import com.kyn.inventory.application.service.interfaces.WarehouseService;
import com.kyn.inventory.application.util.FormatUtil;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.RequestParam;

import com.kyn.inventory.application.entity.Warehouse;



@RestController
@RequestMapping("warehouse")
public class RestWebController {
    
    private final WarehouseService warehouseService;

    private final WarehouseRepository repository;
    public RestWebController(WarehouseService warehouseService, WarehouseRepository repository) {
        this.warehouseService = warehouseService;
        this.repository = repository;
    }

    @GetMapping("getAll")
    public Flux<CurrentStock> getMethodName() {
        String snapshotDate =FormatUtil.DATE_FORMATTER.format(LocalDate.now());
        return warehouseService.findDailyStockSummary(snapshotDate);
    }

    @GetMapping("getAllN")
    public Flux<Warehouse> getMethodName2() {
        return repository.findAll();
    }
    
    
    
}

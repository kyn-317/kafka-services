package com.kyn.inventory.application.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyn.inventory.application.mapper.EntityDtoMapper;
import com.kyn.inventory.application.repository.WarehouseHistoryRepository;
import com.kyn.inventory.application.repository.WarehouseRepository;
import com.kyn.inventory.application.service.interfaces.WarehouseSchedulerService;
import com.kyn.inventory.application.util.FormatUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service

@Slf4j
public class WarehouseSchedulerServiceImpl implements WarehouseSchedulerService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseHistoryRepository warehouseHistoryRepository;
    
    public WarehouseSchedulerServiceImpl(WarehouseRepository warehouseRepository, WarehouseHistoryRepository warehouseHistoryRepository) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseHistoryRepository = warehouseHistoryRepository;
    }
    
    // Daily warehouse batch process
    @Scheduled(cron = "0 0 0 * * ?") 
    @Transactional
    @Override
    public Mono<Void> processWarehouseDailyBatch() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        
        String yesterdayStr = yesterday.format(FormatUtil.DATE_FORMATTER);
        String todayStr = today.format(FormatUtil.DATE_FORMATTER);
        
        log.info("Starting daily warehouse batch process: {} -> {}", yesterdayStr, todayStr);
        //  save in history 
        return warehouseRepository.findBySnapshotDate(yesterdayStr)
            .map(EntityDtoMapper::toWarehouseHistory)
            .flatMap(warehouseHistoryRepository::save)
            .collectList()
            //  set base stock
            .flatMap(savedHistories -> 
                warehouseRepository.findDailyStockSummary(yesterdayStr)
                    .map(EntityDtoMapper::setBaseStock)
                    .flatMap(warehouseRepository::save)
                    .collectList()
            )
            //  delete yesterday's data
            .flatMap(savedBases ->
                    warehouseRepository.deleteBySnapshotDate(yesterdayStr)
                    .thenReturn(savedBases.size()))
            .doOnSuccess(count -> log.info("Completed daily warehouse batch process: {} -> {}, processed {} products", 
                         yesterdayStr, todayStr, count))
            .doOnError(e -> log.error("Error in daily warehouse batch process", e))
            .then();
    }
} 
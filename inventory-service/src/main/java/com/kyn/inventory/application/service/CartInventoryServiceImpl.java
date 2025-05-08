package com.kyn.inventory.application.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;
import com.kyn.inventory.application.entity.Warehouse;
import com.kyn.inventory.application.enums.StorageRetrievalType;
import com.kyn.inventory.application.mapper.EntityDtoMapper;
import com.kyn.inventory.application.service.interfaces.WarehouseService;
import com.kyn.inventory.common.service.CartInventoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartInventoryServiceImpl implements CartInventoryService{
    
    private final WarehouseService warehouseService;
    @Override
    @Transactional
    public Mono<CartInventoryResponse> deduct(CartInventoryRequest.Deduct request) {

        return Flux.fromIterable(request.requestItem().getOrderDetails())
        .map( detail -> EntityDtoMapper.detailToRequest(
            detail, request.requestItem().getCustomerId(), StorageRetrievalType.RETRIEVAL))
        .flatMap(this.warehouseService::deduct)
        .collectList()
        .map(list -> list.stream().map(Warehouse::getId).collect(Collectors.toList()))
        .map(list -> EntityDtoMapper.toDeducted(request.requestItem(), list));
        
    }

    @Override
    @Transactional
    public Mono<CartInventoryResponse> restore(CartInventoryRequest.Restore request) {
        
        return Flux.fromIterable(request.requestItem().getOrderDetails())
        .map( detail -> EntityDtoMapper.detailToRequest(
            detail, request.requestItem().getCustomerId(), StorageRetrievalType.RETRIEVAL_CANCEL))
        .flatMap(this.warehouseService::restore)
        .collectList()
        .map(list -> list.stream().map(Warehouse::getId).collect(Collectors.toList()))
        .map(list -> EntityDtoMapper.toDeducted(request.requestItem(), list));
    }

}

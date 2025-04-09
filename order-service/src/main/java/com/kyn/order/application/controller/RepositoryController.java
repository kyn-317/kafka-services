package com.kyn.order.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.order.application.entity.PurchaseOrder;
import com.kyn.order.application.repository.PurchaseOrderRepository;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/order")
public class RepositoryController {
    

    @Autowired private PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping("/purchase-order")
    public Flux<PurchaseOrder> getPurchaseOrder() {
        return purchaseOrderRepository.findAll();
    }
}

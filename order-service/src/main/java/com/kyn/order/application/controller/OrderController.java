package com.kyn.order.application.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.service.CartOrderService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final CartOrderService cartOrderService;



    @PostMapping("byCart")
    public Mono<ResponseEntity<OrderSummaryDto>> placeOrderByCart(@RequestBody OrderByCart orderByCart) {
        return this.cartOrderService.placeOrderByCart(orderByCart)
                   .map(ResponseEntity.accepted()::body);
    }


}

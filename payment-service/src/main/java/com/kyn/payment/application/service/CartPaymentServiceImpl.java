package com.kyn.payment.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.payment.common.service.CartPaymentDto;
import com.kyn.payment.common.service.CartPaymentProcessRequest;
import com.kyn.payment.common.service.CartPaymentService;

import reactor.core.publisher.Mono;

@Service
public class CartPaymentServiceImpl implements CartPaymentService {

    @Override
    public Mono<CartPaymentDto> process(CartPaymentProcessRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'process'");
    }

    @Override
    public Mono<CartPaymentDto> refund(UUID orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refund'");
    }
    
    
}

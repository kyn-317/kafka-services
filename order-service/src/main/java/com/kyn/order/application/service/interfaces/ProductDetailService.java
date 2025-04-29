package com.kyn.order.application.service.interfaces;

import java.util.List;

import com.kyn.common.dto.CartItem;
import com.kyn.common.dto.CartResponse;
import com.kyn.common.dto.ProductBasDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductDetailService {
    Mono<CartResponse> getCart(String email);
    Flux<ProductBasDto> getCartByCartId(List<CartItem> cartItems);
    Mono<ProductBasDto> getProduct(String productId);
}

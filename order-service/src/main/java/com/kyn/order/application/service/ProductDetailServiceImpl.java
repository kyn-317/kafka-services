package com.kyn.order.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kyn.common.dto.CartItem;
import com.kyn.common.dto.CartResponse;
import com.kyn.common.dto.ProductBasDto;
import com.kyn.order.application.service.interfaces.ProductDetailService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    private final WebClient webClient;

    private final RMapCacheReactive<String, ProductBasDto> productBasMap;

    public ProductDetailServiceImpl(WebClient webClient, RedissonClient redissonClient) {
        this.webClient = webClient;
        this.productBasMap = redissonClient.reactive().getMapCache("product:id");
    }
    @Override
    public Mono<CartResponse> getCart(String email) {
        return this.webClient.get()
            .uri("/getCartByEmail/{email}", email)
            .retrieve()
            .bodyToMono(CartResponse.class);
    }

    @Override
    public Mono<ProductBasDto> getProduct(String productId) {
        return
        this.productBasMap.get(productId)
        .switchIfEmpty(
            this.webClient.get()
            .uri("/findProductById/{productId}", productId)
            .retrieve()
            .bodyToMono(ProductBasDto.class)
        );
    }
    @Override
    public Flux<ProductBasDto> getCartByCartId(List<CartItem> cartItems) {
    
        List<String> productIds = cartItems.stream()
        .map(CartItem::productId)
        .collect(Collectors.toList());

        return this.webClient.post()
        .uri("/findProductsByIds")
        .bodyValue(productIds)
        .retrieve()
        .bodyToFlux(ProductBasDto.class);

    }

    
    
} 


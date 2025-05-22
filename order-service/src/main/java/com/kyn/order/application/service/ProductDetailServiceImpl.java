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

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    private final WebClient productServiceWebClient;

    private final RMapCacheReactive<String, ProductBasDto> productBasMap;

    public ProductDetailServiceImpl(WebClient productServiceWebClient, RedissonClient redissonClient) {
        this.productServiceWebClient = productServiceWebClient;
        this.productBasMap = redissonClient.reactive().getMapCache("product:id");
    }
    @Override
    public Mono<CartResponse> getCart(String email) {
        return this.productServiceWebClient.get()
            .uri("getCartByEmail/{email}", email)
            .retrieve()
            .bodyToMono(CartResponse.class);
    }

    @Override
    public Mono<ProductBasDto> getProduct(String productId) {
        return
        this.productBasMap.get(productId)
        .switchIfEmpty(
            this.productServiceWebClient.get()
            .uri("findProductById/{productId}", productId)
            .retrieve()
            .bodyToMono(ProductBasDto.class)
        );
    }
    @Override
    public Flux<ProductBasDto> getCartByCartId(List<CartItem> cartItems) {
    
        List<String> productIds = cartItems.stream()
        .map(CartItem::productId)
        .collect(Collectors.toList());
        log.info("productIds: {}", productIds);

        return this.productServiceWebClient.post()
        .uri("findProductByIds")
        .bodyValue(productIds)
        .retrieve()
        .bodyToFlux(ProductBasDto.class)
        .doOnError(error -> log.error("error: {}", error));

    }

    
    
} 


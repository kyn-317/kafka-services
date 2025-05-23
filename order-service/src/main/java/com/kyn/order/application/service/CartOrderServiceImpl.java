package com.kyn.order.application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.application.entity.OrderDetail;
import com.kyn.order.application.mapper.CartMapper;
import com.kyn.order.application.repository.OrderDetailRepository;
import com.kyn.order.application.repository.OrderRepository;
import com.kyn.order.application.service.interfaces.ProductDetailService;
import com.kyn.order.common.dto.CartOrderDetails;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.service.CartEventListener;
import com.kyn.order.common.service.CartOrderService;
import com.kyn.order.common.service.CartWorkflowActionRetriever;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CartOrderServiceImpl implements CartOrderService {

    private final OrderRepository repository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartWorkflowActionRetriever actionRetriever;
    private final ProductDetailService productDetailService;
    private final CartEventListener cartEventListener;

    public CartOrderServiceImpl(OrderRepository repository, OrderDetailRepository orderDetailRepository,
     ProductDetailService productDetailService, CartEventListener cartEventListener, CartWorkflowActionRetriever actionRetriever) {
        this.repository = repository;
        this.orderDetailRepository = orderDetailRepository;
        this.productDetailService = productDetailService;
        this.cartEventListener = cartEventListener;
        this.actionRetriever = actionRetriever;
    }


    @Override
    public Flux<OrderSummaryDto> getAllOrders() {
        return this.repository.findAll()
        .flatMap(order -> 
            this.orderDetailRepository.findByOrderId(order.getOrderId())
            .collectList()
            .map(details -> CartMapper.toOrderSummary(order, details))
        );
        
    }

    @Override
    public Mono<CartOrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                              .map(CartMapper::toOrderDto)
                              .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                              .map(t -> CartMapper.toCartOrderDetails(t.getT1(), t.getT2()));
    }

    @Override
    public Mono<OrderSummaryDto> placeOrderByCart(OrderByCart cart) {
        var customerId = cart.customerId().toString();
        return productDetailService.getCartByCartId(cart.cart().cartItems()).collectList()
            .map(productBasDtos -> CartMapper.toCartResponse(cart, productBasDtos))
            .map(item -> OrderByCart.builder().customerId(UUID.fromString(customerId)).cart(item).build())
            .flatMap(this::placeOrder)
            .doOnNext(cartEventListener::emitOrderCreated);
    }

    private Mono<OrderSummaryDto> placeOrder(OrderByCart cart) {
     // Order create
     var order = CartMapper.toOrder(cart.cart(), cart.customerId().toString());
     order.insertDocument(cart.cart().email());
        return this.repository.save(order)
        .flatMap(savedOrder -> { 
        List<OrderDetail> orderDetails = cart.cart().cartItems().stream()
        .map(cartItem -> {
            var orderDetail = CartMapper.toOrderDetail(cartItem, savedOrder.getOrderId());
            orderDetail.insertDocument(savedOrder.getOrderId().toString());
            log.info("orderDetail: {}", orderDetail);
            return orderDetail;
        })
        .collect(Collectors.toList());
        return this.orderDetailRepository.saveAll(orderDetails).collectList()
            .map(savedDetails -> CartMapper.toOrderSummary(savedOrder, savedDetails));
        });
    }
}

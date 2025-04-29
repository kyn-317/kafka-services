package com.kyn.order.application.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kyn.order.application.mapper.CartMapper;
import com.kyn.order.application.mapper.EntityDtoMapper;
import com.kyn.order.application.repository.OrderDetailRepository;
import com.kyn.order.application.repository.OrderRepository;
import com.kyn.order.application.repository.PurchaseOrderRepository;
import com.kyn.order.application.service.interfaces.ProductDetailService;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.dto.OrderCreateRequest;
import com.kyn.order.common.dto.OrderDetails;
import com.kyn.order.common.dto.OrderSummary;
import com.kyn.order.common.dto.PurchaseOrderDto;
import com.kyn.order.common.service.OrderEventListener;
import com.kyn.order.common.service.OrderService;
import com.kyn.order.common.service.WorkflowActionRetriever;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionRetriever actionRetriever;
    private final ProductDetailService productDetailService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request) {
        var entity = EntityDtoMapper.toPurchaseOrder(request);
        return this.repository.save(entity)
                              .map(EntityDtoMapper::toPurchaseOrderDto)
                              .doOnNext(eventListener::emitOrderCreated);
    }

    @Override
    public Flux<PurchaseOrderDto> getAllOrders() {
        return this.repository.findAll()
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                              .map(EntityDtoMapper::toPurchaseOrderDto)
                              .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                              .map(t -> EntityDtoMapper.toOrderDetails(t.getT1(), t.getT2()));
    }

    @Override
    public Mono<OrderSummary> placeOrderByCart(OrderByCart cart) {
        var customerId = cart.customerId().toString();
        return productDetailService.getCartByCartId(cart.cart().cartItems()).collectList()
            .map(productBasDtos -> CartMapper.toCartResponse(cart, productBasDtos))
            .map( item -> OrderByCart.builder().customerId(UUID.fromString(customerId)).cart(item).build())
            .flatMap(this::placeOrder);
    }

    private Mono<OrderSummary> placeOrder(OrderByCart cart) {
     // Order create
        return this.orderRepository.save(CartMapper.toOrder(cart.cart(), cart.customerId().toString()))
        .flatMap(savedOrder -> 
            this.orderDetailRepository.saveAll(cart.cart().cartItems().stream()
            .map(cartItem -> CartMapper.toOrderDetail(cartItem, savedOrder.getOrderId()))
            .collect(Collectors.toList())).collectList()
            .map(savedDetails -> CartMapper.toOrderSummary(savedOrder, savedDetails))
    );
    }
}

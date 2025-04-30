package com.kyn.order.application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.order.application.entity.OrderDetail;
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
import com.kyn.order.common.service.CartEventListener;
import com.kyn.order.common.service.OrderEventListener;
import com.kyn.order.common.service.OrderService;
import com.kyn.order.common.service.WorkflowActionRetriever;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionRetriever actionRetriever;
    private final ProductDetailService productDetailService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartEventListener cartEventListener;

    public OrderServiceImpl(PurchaseOrderRepository repository, OrderEventListener eventListener, WorkflowActionRetriever actionRetriever,
     ProductDetailService productDetailService, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, CartEventListener cartEventListener) {
        this.repository = repository;
        this.eventListener = eventListener;
        this.actionRetriever = actionRetriever;
        this.productDetailService = productDetailService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.cartEventListener = cartEventListener;
    }

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
    public Mono<OrderSummaryDto> placeOrderByCart(OrderByCart cart) {
        var customerId = cart.customerId().toString();
        return productDetailService.getCartByCartId(cart.cart().cartItems()).collectList()
            .map(productBasDtos -> CartMapper.toCartResponse(cart, productBasDtos))
            .map( item -> OrderByCart.builder().customerId(UUID.fromString(customerId)).cart(item).build())
            .flatMap(this::placeOrder)
            .doOnNext(cartEventListener::emitOrderCreated);
    }

    private Mono<OrderSummaryDto> placeOrder(OrderByCart cart) {
     // Order create
     var order = CartMapper.toOrder(cart.cart(), cart.customerId().toString());
     order.insertDocument(cart.cart().email());
        return this.orderRepository.save(order)
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

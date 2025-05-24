package com.kyn.order.application.mapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.kyn.common.dto.CartItem;
import com.kyn.common.dto.CartResponse;
import com.kyn.common.dto.OrderDetailDto;
import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.dto.ProductBasDto;
import com.kyn.order.application.entity.Order;
import com.kyn.order.application.entity.OrderDetail;
import com.kyn.order.application.entity.OrderWorkFlowAction;
import com.kyn.order.common.dto.CartOrderDetails;
import com.kyn.order.common.dto.CartOrderWorkflowActionDto;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.dto.OrderDto;
import com.kyn.order.common.dto.OrderWorkflowActionDto;
import com.kyn.order.common.enums.OrderStatus;
import com.kyn.order.common.enums.WorkflowAction;

public class CartMapper {
    
    public static CartResponse toCartResponse(OrderByCart cart, List<ProductBasDto> productBasDtos) {
        // convert To map
        Map<String, ProductBasDto> productMap = productBasDtos.stream()
        .collect(Collectors.toMap(ProductBasDto::_id, product -> product));
        // getCartItems
        List<CartItem> updatedCartItems = cart.cart().cartItems().stream()
            .map(cartItem -> {
                ProductBasDto product = productMap.get(cartItem.productId());
                if (product == null) {
                    throw new RuntimeException("Product not found: " + cartItem.productId());
                }                
                if (!Objects.equals(cartItem.productPrice(), product.productPrice())) {
                    throw new RuntimeException(
                        String.format("Price mismatch for product %s: Cart price %d != Product price %d",
                            cartItem.productId(), cartItem.productPrice(), product.productPrice())
                    );
                }
                
                return new CartItem(
                    cartItem.productId(),
                    product.productName(),
                    product.productPrice(),
                    product.productImage(),
                    cartItem.productQuantity()
                );
            })
            .collect(Collectors.toList());

        return new CartResponse(
            cart.cart()._id(),
            cart.cart().email(),
            updatedCartItems,
            cart.cart().totalPrice()
        );
    }

    public static Order toOrder(CartResponse cartResponse, String customerId) {
        return Order.builder()
        .customerId(UUID.fromString(customerId))
        .totalPrice(cartResponse.totalPrice())
        .status(OrderStatus.PENDING)
        .build();
    }

    public static OrderDetail toOrderDetail(CartItem cartItem, UUID orderId) {
        return OrderDetail.builder()
        .orderId(orderId)
        .productId(UUID.fromString(cartItem.productId()))
        .quantity(cartItem.productQuantity())
        .unitPrice(cartItem.productPrice())
        .amount(cartItem.productPrice() * cartItem.productQuantity())
        .build();
    }

    public static OrderSummaryDto toOrderSummary(Order order, List<OrderDetail> orderDetails) {
        return OrderSummaryDto.builder()
        .orderId(order.getOrderId())
        .customerId(order.getCustomerId())
        .totalPrice(order.getTotalPrice())
        .orderDetails(orderDetails.stream().map(CartMapper::toOrderDetailDto).collect(Collectors.toList()))
        .build();
    }

    private static OrderDetailDto toOrderDetailDto(OrderDetail orderDetail) {
        return OrderDetailDto.builder()
        .orderDetailId(orderDetail.getOrderDetailId())
        .orderId(orderDetail.getOrderId())
        .productId(orderDetail.getProductId())
        .build();
    }

    public static OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
        .orderId(order.getOrderId())
        .customerId(order.getCustomerId())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus())
        .build();
    }

    public static CartOrderWorkflowActionDto toCartOrderWorkflowActionDto(OrderWorkFlowAction orderWorkflowAction) {
        return CartOrderWorkflowActionDto.builder()
        .id(orderWorkflowAction.getOrderWorkflowActionId())
        .orderId(orderWorkflowAction.getOrderId())
        .action(orderWorkflowAction.getAction())
        .build();
    }

    public static CartOrderDetails toCartOrderDetails(OrderDto order, List<CartOrderWorkflowActionDto> actions) {
        return CartOrderDetails.builder()
        .order(order)
        .actions(actions)
        .build();
    }

        public static OrderWorkFlowAction toOrderWorkflowAction(UUID orderId, WorkflowAction action) {
        return OrderWorkFlowAction.builder()
                                  .orderId(orderId)
                                  .action(action)
                                  .build();
    }

    public static OrderWorkflowActionDto toOrderWorkflowActionDto(OrderWorkFlowAction orderWorkflowAction) {
        return OrderWorkflowActionDto.builder()
                                     .id(orderWorkflowAction.getOrderWorkflowActionId())
                                     .orderId(orderWorkflowAction.getOrderId())
                                     .action(orderWorkflowAction.getAction())
                                     .build();
    }
}

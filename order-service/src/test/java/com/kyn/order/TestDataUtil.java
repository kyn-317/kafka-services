package com.kyn.order;

import java.util.List;
import java.util.UUID;

import com.kyn.common.dto.CartItem;
import com.kyn.common.dto.CartResponse;
import com.kyn.order.common.dto.CartOrderDetails;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.dto.OrderCreateRequest;

public class TestDataUtil {

    public static OrderCreateRequest toRequest(UUID customerId, UUID productId, int unitPrice, int quantity) {
        return OrderCreateRequest.builder()
                                      .unitPrice(unitPrice)
                                      .quantity(quantity)
                                      .customerId(customerId)
                                      .productId(productId)
                                      .build();
    }

    public static OrderByCart toOrderByCart(UUID customerId, List<CartItem> cartItems, String email) {
        var totalPrice = cartItems.stream().mapToDouble( cartItem -> cartItem.productPrice() * cartItem.productQuantity()).sum();
        return OrderByCart.builder()
                              .customerId(customerId)
                              .cart(CartResponse.builder()
                              ._id(UUID.randomUUID().toString())
                              .email(email)
                              .cartItems(cartItems)
                              .totalPrice(totalPrice)
                              .build())
                              .build();
    }

}
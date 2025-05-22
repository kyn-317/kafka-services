package com.kyn.common.dto;

public record CartItem(
    String productId,
    String productName,
    Double productPrice,
    String productImage,
    int productQuantity
) {
    
}

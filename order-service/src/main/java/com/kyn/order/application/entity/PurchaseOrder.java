package com.kyn.order.application.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.kyn.order.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "order_data", name = "purchase_order")
public class PurchaseOrder {

    @Id
    private UUID orderId;
    private UUID customerId;
    private UUID productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer amount;
    private Integer finalAmount;
    private OrderStatus status;
    private Instant deliveryDate;

}
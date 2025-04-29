package com.kyn.order.application.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.kyn.order.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Order extends BaseDocuments{
    @Id
    private UUID orderId;
    private UUID customerId;
    private Double totalPrice; 
    private OrderStatus status;   
}

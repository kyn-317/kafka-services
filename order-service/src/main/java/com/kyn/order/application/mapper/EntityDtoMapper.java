package com.kyn.order.application.mapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.kyn.order.application.entity.OrderWorkFlowAction;
import com.kyn.order.application.entity.PurchaseOrder;
import com.kyn.order.common.dto.PurchaseOrderDto;
import com.kyn.order.common.dto.OrderCreateRequest;
import com.kyn.order.common.dto.OrderDetails;
import com.kyn.order.common.dto.OrderWorkflowActionDto;
import com.kyn.order.common.enums.OrderStatus;
import com.kyn.order.common.enums.WorkflowAction;

public class EntityDtoMapper {

    public static PurchaseOrderDto toDto(PurchaseOrder entity) {
        return PurchaseOrderDto.builder()
                .orderId(entity.getOrderId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .deliveryDate(entity.getDeliveryDate())
                .build();
    }

    
    public static PurchaseOrderDto toPurchaseOrderDto(PurchaseOrder purchaseOrder) {
        return PurchaseOrderDto.builder()
                               .orderId(purchaseOrder.getOrderId())
                               .unitPrice(purchaseOrder.getUnitPrice())
                               .quantity(purchaseOrder.getQuantity())
                               .productId(purchaseOrder.getProductId())
                               .amount(purchaseOrder.getAmount())
                               .customerId(purchaseOrder.getCustomerId())
                               .status(purchaseOrder.getStatus())
                               .deliveryDate(purchaseOrder.getDeliveryDate())
                               .build();
    }

    public static OrderWorkFlowAction toOrderWorkflowAction(UUID orderId, WorkflowAction action) {
        return OrderWorkFlowAction.builder()
                                  .orderId(orderId)
                                  .action(action)
                                  .createdAt(Instant.now())
                                  .build();
    }

    public static OrderWorkflowActionDto toOrderWorkflowActionDto(OrderWorkFlowAction orderWorkflowAction) {
        return OrderWorkflowActionDto.builder()
                                     .id(orderWorkflowAction.getId())
                                     .orderId(orderWorkflowAction.getOrderId())
                                     .createdAt(orderWorkflowAction.getCreatedAt())
                                     .action(orderWorkflowAction.getAction())
                                     .build();
    }

    public static OrderDetails toOrderDetails(PurchaseOrderDto order, List<OrderWorkflowActionDto> actions) {
        return OrderDetails.builder()
                           .order(order)
                           .actions(actions)
                           .build();
    }
} 
package com.kyn.order.application.entity;

import com.kyn.order.common.enums.WorkflowAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "order_data", name = "order_workflow_action")
public class OrderWorkFlowAction {

    @Id
    private UUID id;
    private UUID orderId;
    private WorkflowAction action;
    private Instant createdAt;
}

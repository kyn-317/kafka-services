package com.kyn.order.common.dto;

import java.time.Instant;
import java.util.UUID;

import com.kyn.order.common.enums.WorkflowAction;

import lombok.Builder;

@Builder
public record CartOrderWorkflowActionDto(UUID id,
                                     UUID orderId,
                                     WorkflowAction action,
                                     Instant createdAt) {
}

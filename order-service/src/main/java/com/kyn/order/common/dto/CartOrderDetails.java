package com.kyn.order.common.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CartOrderDetails(OrderDto order,
                               List<CartOrderWorkflowActionDto> actions) {
    
}

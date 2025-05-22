package com.kyn.order.common.service;

import com.kyn.common.dto.OrderSummaryDto;

public interface CartEventListener {
    void emitOrderCreated(OrderSummaryDto dto);
}

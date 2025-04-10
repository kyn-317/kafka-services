package com.kyn.order.common.service;

import com.kyn.order.common.dto.PurchaseOrderDto;

public interface OrderEventListener {

    void emitOrderCreated(PurchaseOrderDto dto);

}

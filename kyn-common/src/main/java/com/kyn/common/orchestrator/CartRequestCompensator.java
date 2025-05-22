package com.kyn.common.orchestrator;

import org.reactivestreams.Publisher;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;

public interface CartRequestCompensator {

    Publisher<CartRequest> compensate(OrderSummaryDto dto);

}

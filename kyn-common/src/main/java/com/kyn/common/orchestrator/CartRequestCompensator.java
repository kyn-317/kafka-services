package com.kyn.common.orchestrator;

import com.kyn.common.messages.Request;
import org.reactivestreams.Publisher;

import com.kyn.common.dto.OrderSummaryDto;

public interface CartRequestCompensator {

    Publisher<Request> compensate(OrderSummaryDto dto);

}

package com.kyn.common.orchestrator;

import java.util.UUID;

import org.reactivestreams.Publisher;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.Request;

public interface CartRequestSender {
    Publisher<CartRequest> send(OrderSummaryDto dto);
}

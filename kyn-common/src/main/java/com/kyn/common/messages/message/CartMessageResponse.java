package com.kyn.common.messages.message;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.CartResponse;

import lombok.Builder;

public sealed interface CartMessageResponse extends CartResponse {

    @Builder
    record Push(OrderSummaryDto responseItem, String message) implements CartMessageResponse {
    }
}

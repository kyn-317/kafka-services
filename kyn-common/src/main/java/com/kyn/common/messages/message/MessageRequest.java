package com.kyn.common.messages.message;

import java.time.Instant;
import java.util.UUID;

import com.kyn.common.messages.Request;

import lombok.Builder;

public sealed interface MessageRequest extends Request {
    
    
    @Builder
    record Push(UUID orderId, UUID userId, String message) implements MessageRequest {}
}

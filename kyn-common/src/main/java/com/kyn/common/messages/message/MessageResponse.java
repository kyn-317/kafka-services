package com.kyn.common.messages.message;

import java.time.Instant;
import java.util.UUID;

import com.kyn.common.messages.Response;

import lombok.Builder;

public sealed interface MessageResponse extends Response{
    
    @Builder
    record Push(UUID messageId,
                UUID orderId,
                UUID userId,
                String message) implements MessageResponse {

}}

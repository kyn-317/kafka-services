package com.kyn.message.common.dto;

import java.time.Instant;
import java.util.UUID;

public record PushRequest(UUID orderId,
                          UUID userId,
                          String message) {
    
}

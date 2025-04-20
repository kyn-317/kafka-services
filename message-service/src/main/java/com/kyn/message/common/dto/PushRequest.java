package com.kyn.message.common.dto;


import java.util.UUID;

public record PushRequest(UUID orderId,
                          UUID userId,
                          String message) {
    
}

package com.kyn.message.common.dto;

import java.util.UUID;


import lombok.Builder;

@Builder
public record MessageDto(UUID messageId,
                          UUID orderId,
                          UUID userId,
                          String message) {
}

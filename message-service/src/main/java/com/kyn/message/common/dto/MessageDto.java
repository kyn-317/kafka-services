package com.kyn.message.common.dto;

import java.time.Instant;
import java.util.UUID;

import com.kyn.common.messages.message.MessageStatus;

import lombok.Builder;

@Builder
public record MessageDto(UUID messageId,
                          UUID orderId,
                          UUID userId,
                          String message) {
}

package com.kyn.message.application.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class SendMessageDto {
    private UUID messageId;
    private UUID userId;
    private UUID orderId;
    private String message;
}
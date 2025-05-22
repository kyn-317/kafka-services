package com.kyn.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MessageHistoryRequest {
    private String userId;
    private String orderId;
    private String message;
}

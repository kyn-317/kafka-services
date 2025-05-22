package com.kyn.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageHistoryResponse {

    private String _id;
    private String userId;
    private String orderId;
    private String message;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
}

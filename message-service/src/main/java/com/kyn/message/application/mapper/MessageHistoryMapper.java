package com.kyn.message.application.mapper;

import java.util.UUID;

import com.kyn.message.application.dto.MessageHistoryRequest;
import com.kyn.message.application.dto.MessageHistoryResponse;
import com.kyn.message.application.entity.MessageHistory;

public class MessageHistoryMapper {
    

    public static MessageHistory HistoryRequestToEntity(MessageHistoryRequest request) {
        return MessageHistory.builder()
            ._id(UUID.randomUUID().toString())
            .userId(request.getUserId())
            .orderId(request.getOrderId())
            .message(request.getMessage())
            .build();
    }
    public static MessageHistoryResponse HistoryEntityToResponse(MessageHistory entity) {
        return MessageHistoryResponse.builder()
            ._id(entity.get_id())
            .userId(entity.getUserId())
            .orderId(entity.getOrderId())
            .message(entity.getMessage())
            .build();
    }
}
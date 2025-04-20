package com.kyn.message.application.mapper;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.bson.types.ObjectId;

import com.kyn.message.application.entity.MessageHistory;
import com.kyn.message.common.dto.MessageDto;

public class EntityDtoMapper {
    
    public static MessageDto toDto(MessageHistory messageHistory) {
        return MessageDto.builder()
            .messageId((messageHistory.get_id()))
            .userId(messageHistory.getUserId())
            .orderId(messageHistory.getOrderId())
            .message(messageHistory.getMessage())
            .build();
    }
}

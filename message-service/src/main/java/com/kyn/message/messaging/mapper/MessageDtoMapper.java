package com.kyn.message.messaging.mapper;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.common.messages.message.MessageResponse;
import com.kyn.message.application.dto.MessageHistoryRequest;
import com.kyn.message.common.dto.MessageDto;
import com.kyn.message.common.dto.PushRequest;

public class MessageDtoMapper {
    
    public static PushRequest toPushRequest(MessageRequest.Push request) {
        return new PushRequest(request.orderId(), 
                                request.userId(),
                                request.message());
    }

    public static MessageResponse toMessageResponse(MessageDto dto) {
        return MessageResponse.Push.builder()
                                         .messageId(dto.messageId())
                                         .orderId(dto.orderId())
                                         .userId(dto.userId())
                                         .message(dto.message())
                                         .build();
    }

    public static MessageHistoryRequest toMessageHistoryRequest(String message, String userId) {
        return MessageHistoryRequest.builder()
            .userId(userId)
            .message(message)
            .build();
    }

}

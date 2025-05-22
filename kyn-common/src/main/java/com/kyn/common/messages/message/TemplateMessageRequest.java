package com.kyn.common.messages.message;

import java.util.Map;

import com.kyn.common.messages.MapRequest;

import lombok.Builder;

public sealed interface TemplateMessageRequest extends MapRequest {
    
    @Builder
    record ORDER_COMPLETED(Map<String,String> requestItem, String userId, String orderId) implements TemplateMessageRequest {}

    @Builder
    record ORDER_CANCELLED(Map<String,String> requestItem, String userId, String orderId) implements TemplateMessageRequest {}
    
    @Builder
    record STOCK_RECEIVED(Map<String,String> requestItem, String userId) implements TemplateMessageRequest {}


}

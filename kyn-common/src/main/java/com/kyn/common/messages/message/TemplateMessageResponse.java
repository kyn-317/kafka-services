package com.kyn.common.messages.message;

import java.util.Map;

import com.kyn.common.messages.MapResponse;

import lombok.Builder;

public sealed interface TemplateMessageResponse extends MapResponse {
    
    @Builder
    record ORDER_COMPLETED(Map<String,String> responseItem) implements TemplateMessageResponse {}

    @Builder
    record ORDER_CANCELLED(Map<String,String> responseItem) implements TemplateMessageResponse {}
    
    @Builder
    record STOCK_RECEIVED(Map<String,String> responseItem) implements TemplateMessageResponse {}


}

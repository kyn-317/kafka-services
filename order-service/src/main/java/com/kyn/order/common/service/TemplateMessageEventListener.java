package com.kyn.order.common.service;

import com.kyn.common.messages.message.TemplateMessageRequest;

public interface TemplateMessageEventListener {
    void createMessage(TemplateMessageRequest message);
}

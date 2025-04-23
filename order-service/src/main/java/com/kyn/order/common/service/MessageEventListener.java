package com.kyn.order.common.service;

import com.kyn.common.messages.message.MessageRequest;

public interface MessageEventListener {
    void createMessage(MessageRequest message);
}

package com.kyn.message.application.service.interfaces;

import com.kyn.common.messages.message.MessageRequest;
import com.kyn.message.common.dto.MessageDto;
import com.kyn.message.common.dto.PushRequest;

import reactor.core.publisher.Mono;

public interface MessagingService {
    Mono<MessageDto> push(PushRequest request);

}

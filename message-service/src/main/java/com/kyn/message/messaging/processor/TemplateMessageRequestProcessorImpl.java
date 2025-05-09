package com.kyn.message.messaging.processor;

import com.kyn.common.messages.message.TemplateMessageRequest.ORDER_CANCELLED;
import com.kyn.common.messages.message.TemplateMessageRequest.ORDER_COMPLETED;
import com.kyn.common.messages.message.TemplateMessageRequest.STOCK_RECEIVED;

import com.kyn.message.application.service.interfaces.MessageHistoryService;
import com.kyn.message.application.service.interfaces.MessageTemplateService;
import com.kyn.message.application.service.interfaces.MessagingService;
import com.kyn.message.messaging.mapper.MessageDtoMapper;

import reactor.core.publisher.Mono;

public class TemplateMessageRequestProcessorImpl implements TemplateMessageRequestProcessor {

    private final MessageTemplateService messageTemplateService;
    private final MessagingService messagingService;
    private final MessageHistoryService messageHistoryService;
    
    public TemplateMessageRequestProcessorImpl(MessageTemplateService messageTemplateService, 
                                             MessagingService messagingService, 
                                             MessageHistoryService messageHistoryService) {
        this.messageTemplateService = messageTemplateService;
        this.messagingService = messagingService;
        this.messageHistoryService = messageHistoryService;
    }

    @Override
    public Mono<Void> handle(ORDER_COMPLETED request) {
        return messageTemplateService.setTemplateMessage(request.requestItem(), 1)
            .flatMap(templateMessage -> 
                messagingService.push(templateMessage, request.requestItem().get("userId"), "ORDER_COMPLETED")
                    .then(messageHistoryService.save(MessageDtoMapper.toMessageHistoryRequest(
                        templateMessage, 
                        request.requestItem().get("userId")
                    )))
            )
            .then();
    }

    @Override
    public Mono<Void> handle(ORDER_CANCELLED request) {
        return messageTemplateService.setTemplateMessage(request.requestItem(), 2)
            .flatMap(templateMessage -> 
                messagingService.push(templateMessage, request.requestItem().get("userId"), "ORDER_CANCELLED")
                    .then(messageHistoryService.save(MessageDtoMapper.toMessageHistoryRequest(
                        templateMessage, 
                        request.requestItem().get("userId")
                    )))
            )
            .then();
    }

    @Override
    public Mono<Void> handle(STOCK_RECEIVED request) {
        return messageTemplateService.setTemplateMessage(request.requestItem(), 3)
            .flatMap(templateMessage -> 
                messagingService.push(templateMessage, request.requestItem().get("userId"), "STOCK_RECEIVED")
                    .then(messageHistoryService.save(MessageDtoMapper.toMessageHistoryRequest(
                        templateMessage, 
                        request.requestItem().get("userId")
                    )))
            )
            .then();
    }
}

package com.kyn.message.application.service;

import org.springframework.stereotype.Service;

import com.kyn.message.application.dto.MessageHistoryRequest;
import com.kyn.message.application.dto.MessageHistoryResponse;
import com.kyn.message.application.mapper.MessageHistoryMapper;
import com.kyn.message.application.repository.MessageHistoryRepository;
import com.kyn.message.application.service.interfaces.MessageHistoryService;

import reactor.core.publisher.Mono;

@Service
public class MessageHistoryServiceImpl implements MessageHistoryService {

    private final MessageHistoryRepository messageHistoryRepository;

    public MessageHistoryServiceImpl(MessageHistoryRepository messageHistoryRepository) {
        this.messageHistoryRepository = messageHistoryRepository;
    }

    @Override
    public Mono<MessageHistoryResponse> save(MessageHistoryRequest messageHistoryRequest) {
        return messageHistoryRepository
                .save(MessageHistoryMapper.HistoryRequestToEntity(messageHistoryRequest))
                .map(MessageHistoryMapper::HistoryEntityToResponse);
    }
}
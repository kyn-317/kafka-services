package com.kyn.message.application.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.kyn.message.application.entity.MessageTemplate;

import reactor.core.publisher.Mono;

@Repository
public interface MessageTemplateRepository extends ReactiveMongoRepository<MessageTemplate, String> {
    
    Mono<MessageTemplate> findByTemplateNumber(Integer templateNumber);
}

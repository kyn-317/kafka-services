package com.kyn.message.application.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.kyn.message.application.entity.MessageHistory;

@Repository
public interface MessageHistoryRepository extends ReactiveMongoRepository<MessageHistory, String> {
}

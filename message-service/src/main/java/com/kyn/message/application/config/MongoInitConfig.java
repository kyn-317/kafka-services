package com.kyn.message.application.config;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.kyn.message.application.entity.MessageTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MongoInitConfig {

    private final ReactiveMongoTemplate mongoTemplate;

    @Bean
    public CommandLineRunner initMongo() {
        return args -> {
            // initialize collections and load product.jsonl file
            initializeCollections()
                    .then(insertMessageTemplate())
                    .subscribe(
                            count -> log.info("load {} products.", count),
                            error -> log.error("error: {}", error.getMessage()));
        };
    }

    private Mono<Void> initializeCollections() {
        // PRODUCT_BAS collection initialization
        Mono<Void> messageHistoryCollection = mongoTemplate.collectionExists("MESSAGE_HISTORY")
                .flatMap(exists -> {
                    if (exists) {
                        log.info("MESSAGE_HISTORY collection exists. clear it.");
                        return mongoTemplate.dropCollection("MESSAGE_HISTORY")
                                .then(mongoTemplate.createCollection("MESSAGE_HISTORY", CollectionOptions.empty()));
                    } else {
                        log.info("create MESSAGE_HISTORY collection.");
                        return mongoTemplate.createCollection("MESSAGE_HISTORY", CollectionOptions.empty());
                    }
                }).then();

        // MESSAGE_TEMPLATE collection initialization
        Mono<Void> messageTemplateCollection = mongoTemplate.collectionExists("MESSAGE_TEMPLATE")
                .flatMap(exists -> {
                    if (exists) {
                        log.info("MESSAGE_TEMPLATE collection exists. clear it.");
                        return mongoTemplate.dropCollection("MESSAGE_TEMPLATE")
                                .then(mongoTemplate.createCollection("MESSAGE_TEMPLATE", CollectionOptions.empty()));
                    } else {
                        log.info("create MESSAGE_TEMPLATE collection.");
                        return mongoTemplate.createCollection("MESSAGE_TEMPLATE", CollectionOptions.empty());
                    }
                }).then();

        // initialize two collections in parallel
        return Mono.when(messageHistoryCollection, messageTemplateCollection);

    }

    String TEMPLATE_MESSAGE_001 = """
            {user_name}, your order {order_details} is completed
            """;
    String TEMPLATE_MESSAGE_002 = """
            {user_name}, your order {order_details} is cancelled
            """;

    String TEMPLATE_MESSAGE_003 = """
            {user_name}, the inventory of your favorite item {product_name} has been added.
            """;

    private Mono<Void> insertMessageTemplate() {
        MessageTemplate messageTemplate = new MessageTemplate();
        messageTemplate.set_id("22ec34fe-b6b1-48a1-9882-4c74cc80b7e7");
        messageTemplate.setTemplateNumber(1);
        messageTemplate.setTemplateName("TEMPLATE_MESSAGE_001");
        messageTemplate.setTemplateContent(TEMPLATE_MESSAGE_001);
        messageTemplate.setTemplateType("ORDER_COMPLETED");
        messageTemplate.insertDocument("SYSTEM");

        MessageTemplate messageTemplate2 = new MessageTemplate();
        messageTemplate2.set_id("1320f806-dfad-4b28-8f01-beef6d39ddc1");
        messageTemplate2.setTemplateNumber(2);
        messageTemplate2.setTemplateName("TEMPLATE_MESSAGE_002");
        messageTemplate2.setTemplateContent(TEMPLATE_MESSAGE_002);
        messageTemplate2.setTemplateType("ORDER_CANCELLED");
        messageTemplate2.insertDocument("SYSTEM");

        MessageTemplate messageTemplate3 = new MessageTemplate();
        messageTemplate3.set_id("9e06ce88-1e8d-4065-bd62-6f7147fe937f");
        messageTemplate3.setTemplateNumber(3);
        messageTemplate3.setTemplateName("TEMPLATE_MESSAGE_003");
        messageTemplate3.setTemplateContent(TEMPLATE_MESSAGE_003);
        messageTemplate3.setTemplateType("STOCK_RECEIVED");
        messageTemplate3.insertDocument("SYSTEM");

        List<MessageTemplate> messageTemplates = List.of(messageTemplate, messageTemplate2, messageTemplate3);

        return mongoTemplate.insert(messageTemplates, MessageTemplate.class).then(Mono.empty());
    }

}
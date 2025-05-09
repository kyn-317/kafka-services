package com.kyn.message.application.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.kyn.message.application.repository.MessageTemplateRepository;
import com.kyn.message.application.service.interfaces.MessageTemplateService;

import reactor.core.publisher.Mono;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateRepository repository;

    public MessageTemplateServiceImpl(MessageTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<String> setTemplateMessage(Map<String, String> requestItem, int templateNumber) {
        return repository.findByTemplateNumber(templateNumber)
            .map(template -> {
                String templateContent = template.getTemplateContent();
                for (Map.Entry<String, String> entry : requestItem.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    templateContent = templateContent.replace("{" + key + "}", value);
                }
                return templateContent;
            });
    }
    
    
}

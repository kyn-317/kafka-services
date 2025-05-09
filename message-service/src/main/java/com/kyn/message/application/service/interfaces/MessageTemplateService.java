package com.kyn.message.application.service.interfaces;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface MessageTemplateService {

    Mono<String> setTemplateMessage(Map<String,String> requestItem, int templateNumber);
    
}

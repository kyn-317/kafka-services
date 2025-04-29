package com.kyn.order.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;



@Configuration
public class WebClientConfig {

    private final String productServiceUrl = "http://localhost:8081";

    @Bean(name = "productServiceWebClient")
    public WebClient productServiceWebClient() {
        return WebClient.builder()
            .baseUrl(productServiceUrl)
            .build();
    }
}

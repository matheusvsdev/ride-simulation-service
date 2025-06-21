package com.matheusvsdev.ridesimulationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${open.route.service.api.token}")
    private String apiToken;

    @Bean
    public WebClient openRouteServiceWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openrouteservice.org")
                .defaultHeader("Authorization", apiToken)
                .build();
    }
}

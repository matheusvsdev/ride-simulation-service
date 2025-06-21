package com.matheusvsdev.ridesimulationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient openRouteServiceWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openrouteservice.org")
                .defaultHeader("Authorization", "5b3ce3597851110001cf6248b33c68e901724d6faaed4b7ab9a3d855")
                .build();
    }
}

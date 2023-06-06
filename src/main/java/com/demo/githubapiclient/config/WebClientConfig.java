package com.demo.githubapiclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${GITHUB_TOKEN}")
    private String githubToken;

    private static final String GITHUB_PREFIX_URI = "https://api.github.com";

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create();
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
                .baseUrl(GITHUB_PREFIX_URI)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken);
                    httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.github+json");
                })
                .filter(addAuthorizationHeader())
                .build();
    }

    private ExchangeFilterFunction addAuthorizationHeader() {
        return (request, next) -> {
            if (!request.headers().containsKey(HttpHeaders.AUTHORIZATION)) {
                request.headers().setBearerAuth(githubToken);
            }
            return next.exchange(request);
        };
    }
}

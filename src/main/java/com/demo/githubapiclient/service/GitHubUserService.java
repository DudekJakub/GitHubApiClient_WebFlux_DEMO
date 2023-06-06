package com.demo.githubapiclient.service;

import com.demo.githubapiclient.exception.UserNotFoundException;
import com.demo.githubapiclient.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GitHubUserService implements UserService {

    private final WebClient webClient;

    @Autowired
    public GitHubUserService(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<User> getUserByUserLogin(final String login) {
        return webClient.get()
                .uri("/users/{login}", login)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new UserNotFoundException(login)))
                .bodyToMono(User.class)
                .doOnNext(user -> log.info("[{}] user successfully found", login));
    }
}

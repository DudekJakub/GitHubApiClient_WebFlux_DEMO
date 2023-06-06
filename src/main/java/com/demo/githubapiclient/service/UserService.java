package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> getUserByUserLogin(String login);
}

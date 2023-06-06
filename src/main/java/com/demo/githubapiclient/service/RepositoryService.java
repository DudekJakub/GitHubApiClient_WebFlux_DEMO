package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RepositoryService {

    Mono<List<Repository>> getUserNotForkedRepositoriesByUserLogin(final String login);
}

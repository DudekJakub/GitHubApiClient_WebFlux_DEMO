package com.demo.githubapiclient.controller;

import com.demo.githubapiclient.mapper.RepositoryMapper;
import com.demo.githubapiclient.model.dto.RepositoryResponseDto;
import com.demo.githubapiclient.service.GitHubRepositoryService;
import com.demo.githubapiclient.service.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final RepositoryService repositoryService;
    private final RepositoryMapper repositoryMapper;

    public GitHubController(final RepositoryService repositoryService, final RepositoryMapper repositoryMapper) {
        this.repositoryService = repositoryService;
        this.repositoryMapper = repositoryMapper;
    }

    @GetMapping(value = "/user/{login}/repositories", produces = "!application/xml")
    public Mono<ResponseEntity<List<RepositoryResponseDto>>> userNotForkedRepositoriesByUserLogin(final @PathVariable String login) {
        return repositoryService.getUserNotForkedRepositoriesByUserLogin(login)
                .map(repositoryMapper::mapRepositoryListToResponseDtoList)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

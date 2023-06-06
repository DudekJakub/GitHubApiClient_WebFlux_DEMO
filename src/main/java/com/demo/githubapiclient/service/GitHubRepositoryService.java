package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class GitHubRepositoryService implements RepositoryService {

    @Value("${GITHUB_LOGIN}")
    private String githubLogin;
    private final BranchService branchService;
    private final UserService userService;
    private final PageFetcherService pageFetcherService;

    @Autowired
    public GitHubRepositoryService(final BranchService branchService, final UserService userService, final PageFetcherService pageFetcherService) {
        this.branchService = branchService;
        this.userService = userService;
        this.pageFetcherService = pageFetcherService;
    }

    public Mono<List<Repository>> getUserNotForkedRepositoriesByUserLogin(final String login) {
        final int resultsPerPage = 100; //max value = 100
        final String pageParam = "&page=1";

        return userService.getUserByUserLogin(login)
                .flatMap(u -> {
                    final String url = u.getLogin().equals(githubLogin) ?
                            String.format("/user/repos?affiliation=owner&per_page=%s%s", resultsPerPage, pageParam) :
                            String.format("/users/%s/repos?per_page=%s%s", login, resultsPerPage, pageParam);

                    return pageFetcherService.fetchAllPages(url, Repository.class)
                            .cast(Repository.class)
                            .filter(repo -> !repo.isFork())
                            .flatMap(repository ->
                                    branchService.fetchBranchesForRepository(repository)
                                            .map(branches -> {
                                                repository.setBranches(branches);
                                                return repository;
                                            }))
                            .collectList()
                            .doOnNext(repositoryList -> log.info("[{}] not forked repositories found for login [{}]", repositoryList.size(), login));
                });
    }
}

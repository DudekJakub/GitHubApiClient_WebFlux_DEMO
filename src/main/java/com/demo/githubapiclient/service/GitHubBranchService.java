package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.Branch;
import com.demo.githubapiclient.model.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class GitHubBranchService implements BranchService {

    private final PageFetcherService pageFetcherService;

    @Autowired
    public GitHubBranchService(final PageFetcherService pageFetcherService) {
        this.pageFetcherService = pageFetcherService;
    }

    public Mono<List<Branch>> fetchBranchesForRepository(final Repository repository) {
        final int resultsPerPage = 100; //max value = 100
        final String pageParam = "&page=1";
        String url = String.format("/repos/%s/%s/branches?per_page=%s%s", repository.getOwner().getLogin(), repository.getName(), resultsPerPage, pageParam);

        return pageFetcherService.fetchAllPages(url, Branch.class)
                .cast(Branch.class)
                .collectList()
                .doOnNext(branchList -> log.info("[{}] branches found for repository [{}]", branchList.size(), repository.getName()));
    }
}

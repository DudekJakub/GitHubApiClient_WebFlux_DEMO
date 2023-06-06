package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.Branch;
import com.demo.githubapiclient.model.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BranchService {

    Mono<List<Branch>> fetchBranchesForRepository(Repository repository);
}

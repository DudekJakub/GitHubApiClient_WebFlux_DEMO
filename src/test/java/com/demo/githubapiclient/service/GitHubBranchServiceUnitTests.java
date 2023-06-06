package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.Branch;
import com.demo.githubapiclient.model.Commit;
import com.demo.githubapiclient.model.Owner;
import com.demo.githubapiclient.model.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubBranchServiceUnitTests {

    @Mock
    private PageFetcherService pageFetcherServiceMock;

    @InjectMocks
    private GitHubBranchService service;

    @Test
    void fetchBranchesForRepository_shouldReturnBranchList() {
        //Given
        String login = "testUser";
        Repository repository = new Repository();
        repository.setOwner(new Owner(login));
        repository.setName("testRepo");
        Branch branch1 = new Branch("testBranch1", new Commit("123"));
        Branch branch2 = new Branch("testBranch2", new Commit("456"));
        List<Branch> branchList = List.of(branch1, branch2);
        repository.setBranches(branchList);

        String url = "/repos/testUser/testRepo/branches?per_page=100&page=1";

        when(pageFetcherServiceMock.fetchAllPages(url, Branch.class)).thenAnswer(invocation -> Flux.just(branch1, branch2));

        //When
        Mono<List<Branch>> result = service.fetchBranchesForRepository(repository);

        //Then
        StepVerifier.create(result)
                .expectNext(branchList)
                .verifyComplete();

        verify(pageFetcherServiceMock, times(1)).fetchAllPages(url, Branch.class);
        verifyNoMoreInteractions(pageFetcherServiceMock);
    }
}
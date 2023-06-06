package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.Owner;
import com.demo.githubapiclient.model.Repository;
import com.demo.githubapiclient.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRepositoryServiceUnitTests {

    @Mock
    private UserService userServiceMock;

    @Mock
    private PageFetcherService pageFetcherServiceMock;

    @Mock
    private BranchService branchServiceMock;

    @InjectMocks
    private GitHubRepositoryService service;

    @Test
    void getUserNotForkedRepositoriesByUserLogin_shouldReturnNonForkedRepositories() {
        //Given
        String login = "testUser";
        User user = new User(login);
        Repository repository1 = new Repository("testRepo1", new Owner(login), false, List.of());
        Repository repository2 = new Repository("testRepo2", new Owner(login), false, List.of());
        Repository repository3 = new Repository("testRepo3", new Owner(login), true, List.of());

        when(userServiceMock.getUserByUserLogin(login)).thenReturn(Mono.just(user));
        when(pageFetcherServiceMock.fetchAllPages(anyString(), eq(Repository.class))).thenAnswer(invocation -> Flux.just(repository1, repository2, repository3));
        when(branchServiceMock.fetchBranchesForRepository(repository1)).thenReturn(Mono.just(Collections.emptyList()));
        when(branchServiceMock.fetchBranchesForRepository(repository2)).thenReturn(Mono.just(Collections.emptyList()));

        //When
        Mono<List<Repository>> resultMono = service.getUserNotForkedRepositoriesByUserLogin(login);

        //Then
        List<Repository> expectedResultList = new ArrayList<>(List.of(repository1, repository2));
        StepVerifier.create(resultMono)
                .expectNext(expectedResultList)
                .verifyComplete();

        verify(userServiceMock, times(1)).getUserByUserLogin(login);
        verify(pageFetcherServiceMock, times(1)).fetchAllPages(anyString(), eq(Repository.class));
        verify(branchServiceMock, times(2)).fetchBranchesForRepository(any());
        verifyNoMoreInteractions(userServiceMock, pageFetcherServiceMock, branchServiceMock);
    }
}
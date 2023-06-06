package com.demo.githubapiclient.service;

import com.demo.githubapiclient.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubUserServiceUnitTests {

    @Mock
    private WebClient webClientMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @InjectMocks
    private GitHubUserService service;

    @SuppressWarnings("unchecked")
    @Test
    void getUserByUserLogin_shouldReturnUser() {
        //Given
        String login = "testUser";
        User user = new User(login);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("/users/{login}", login)).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(User.class)).thenReturn(Mono.just(user));

        //When
        Mono<User> userByUserLogin = service.getUserByUserLogin(login);

        //Then
        StepVerifier.create(userByUserLogin)
                .expectNext(user)
                .verifyComplete();

        verify(webClientMock, times(1)).get();
        verifyNoMoreInteractions(webClientMock);
    }
}
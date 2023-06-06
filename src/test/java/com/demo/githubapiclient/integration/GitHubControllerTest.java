package com.demo.githubapiclient.integration;

import com.demo.githubapiclient.model.dto.ExceptionDto;
import com.demo.githubapiclient.model.dto.RepositoryResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

/** For the purpose of integration tests, a special GitHub test user was created.
 * Only the author of the application - Dudek Jakub has access to the test user.
 * The test user is not subject to any modifications.
 * LINK TO ACCOUNT: https://github.com/TestAccountUser1?tab=repositories
 * */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GitHubControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String realTestUserLogin = "TestAccountUser1";

    @Test
    void testUserNotForkedRepositoriesByUserLogin_shouldReturnOkResponseWithRepositories() {
        //When/Then
        webTestClient.get()
                .uri(String.format("/api/github/user/%s/repositories", realTestUserLogin))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryResponseDto.class)
                .consumeWith(response -> {
                    List<RepositoryResponseDto> dtoList = response.getResponseBody();
                    Assertions.assertNotNull(dtoList);
                    assertEquals(1, dtoList.size());
                    assertEquals(realTestUserLogin, dtoList.get(0).getOwnerLogin());
                    assertEquals("testRepo1", dtoList.get(0).getRepositoryName());
                    assertEquals(2, dtoList.get(0).getBranches().size());
                    assertEquals("master", dtoList.get(0).getBranches().get(0).getBranchName());
                    assertEquals("testBranch", dtoList.get(0).getBranches().get(1).getBranchName());
                    assertTrue(dtoList.get(0).getBranches().get(0).getLastCommitSha().startsWith("59913ea"));
                    assertTrue(dtoList.get(0).getBranches().get(1).getLastCommitSha().startsWith("fd5822b"));
                });
    }

    @Test
    void testUserNotForkedRepositoriesByUserLogin_shouldReturnNotFoundResponse() {
        //Given
        String randomNoneExistentLogin = UUID.randomUUID().toString();

        //When/Then
        webTestClient.get()
                .uri(String.format("/api/github/user/%s/repositories", randomNoneExistentLogin))
                .exchange()
                .expectStatus().isNotFound()
                .expectBodyList(ExceptionDto.class)
                .consumeWith(response -> {
                    List<ExceptionDto> responseBody = response.getResponseBody();
                    Assertions.assertNotNull(responseBody);
                    assertEquals(404, responseBody.get(0).status());
                    assertEquals("Given login [" + randomNoneExistentLogin + "] does not exist.", responseBody.get(0).message());
                });
    }

    @Test
    void testUserNotForkedRepositoriesByUserLogin_withUnacceptableMediaTypeXML_shouldReturnNotAcceptable() {
        //When/Then
        webTestClient.get()
                .uri(String.format("/api/github/user/%s/repositories", realTestUserLogin))
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBodyList(ExceptionDto.class)
                .consumeWith(response -> {
                    List<ExceptionDto> responseBody = response.getResponseBody();
                    Assertions.assertNotNull(responseBody);
                    assertEquals(406, responseBody.get(0).status());
                    assertEquals("406 NOT_ACCEPTABLE \"Could not find acceptable representation\"", responseBody.get(0).message());
                });
    }
}
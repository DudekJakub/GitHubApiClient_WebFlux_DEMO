package com.demo.githubapiclient.aspect;

import com.demo.githubapiclient.model.Repository;
import com.demo.githubapiclient.model.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(value = "logging.aspect.enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAspect {

    @Before("execution(* com.demo.githubapiclient.service.GitHubRepositoryService.getUserNotForkedRepositoriesByUserLogin(..)) && args(login)")
    public void logBeforeGetUserNotForkedRepositoriesByUserLogin(String login) {
        log.debug("Executing getUserNotForkedRepositoriesByUserLogin for login: {}", login);
    }

    @AfterReturning(pointcut = "execution(* com.demo.githubapiclient.service.GitHubRepositoryService.getUserNotForkedRepositoriesByUserLogin(..))", returning = "result")
    public void logAfterReturningUserNotForkedRepositories(Mono<List<Repository>> result) {
        result.subscribe(repositories -> {
            log.debug("User not forked repositories fetched successfully");
            log.debug("Number of repositories: {}", repositories.size());
            for (Repository repository : repositories) {
                log.debug("Repository: {}", repository.getName());
                log.debug("Branches: {}", repository.getBranches());
            }
        });
    }

    @AfterThrowing(pointcut = "execution(* com.demo.githubapiclient.service.GitHubRepositoryService.getUserNotForkedRepositoriesByUserLogin(..))", throwing = "exception")
    public void logAfterThrowingUserNotForkedRepositories(Exception exception) {
        log.debug("Exception thrown in getUserNotForkedRepositoriesByUserLogin: {}", exception.getMessage());
    }

    @AfterThrowing(pointcut = "execution(* com.demo.githubapiclient.service.PageFetcherService.fetchAllPages(..))", throwing = "exception")
    public void logAfterThrowingFetchAllPages(Exception exception) {
        log.debug("Exception thrown in fetchAllPages: {}", exception.getMessage());
    }

    @Before("execution(* com.demo.githubapiclient.service.BranchService.fetchBranchesForRepository(..)) && args(repository)")
    public void logBeforeFetchBranchesForRepository(Repository repository) {
        log.debug("Fetching branches for repository: {}", repository.getName());
    }

    @AfterThrowing(pointcut = "execution(* com.demo.githubapiclient.service.BranchService.fetchBranchesForRepository(..))", throwing = "exception")
    public void logAfterThrowingFetchBranchesForRepository(Exception exception) {
        log.debug("Exception thrown in fetchBranchesForRepository: {}", exception.getMessage());
    }

    @Before("execution(* com.demo.githubapiclient.service.UserService.getUserByUserLogin(..)) && args(login)")
    public void logBeforeGetUserByUserLogin(String login) {
        log.debug("Fetching user with login: {}", login);
    }

    @AfterReturning(pointcut = "execution(* com.demo.githubapiclient.service.UserService.getUserByUserLogin(..))", returning = "result")
    public void logAfterReturningGetUserByUserLogin(Mono<User> result) {
        result.subscribe(user ->
                        log.debug("Received user: {}", user.getLogin()),
                error -> log.debug("Error occurred in getUserByUserLogin: {}", error.getMessage()));
    }

    @AfterThrowing(pointcut = "execution(* com.demo.githubapiclient.service.UserService.getUserByUserLogin(..))", throwing = "exception")
    public void logAfterThrowingGetUserByUserLogin(Exception exception) {
        log.debug("Exception thrown in getUserByUserLogin: {}", exception.getMessage());
    }
}

package com.demo.githubapiclient.exception;

import com.demo.githubapiclient.model.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.NotAcceptableStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final MediaType defaultMediaType = MediaType.APPLICATION_JSON;

    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<ExceptionDto> handleWrongMediaTypeException(RuntimeException e) {
        HttpStatus resultStatus = HttpStatus.NOT_ACCEPTABLE;
        return ResponseEntity
                .status(resultStatus)
                .contentType(defaultMediaType)
                .body(new ExceptionDto(resultStatus.value(), e.getMessage()));
    }

    @ExceptionHandler(GitHubServiceException.class)
    public ResponseEntity<ExceptionDto> handleGitHubServiceException(GitHubServiceException e) {
        HttpStatus resultStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(resultStatus)
                .contentType(defaultMediaType)
                .body(new ExceptionDto(resultStatus.value(), e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleGitHubFileNotFoundException(UserNotFoundException e) {
        HttpStatus resultStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(resultStatus)
                .contentType(defaultMediaType)
                .body(new ExceptionDto(resultStatus.value(), e.getMessage()));
    }
}

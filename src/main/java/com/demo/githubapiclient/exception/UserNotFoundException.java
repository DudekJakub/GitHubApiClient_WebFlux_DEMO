package com.demo.githubapiclient.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String login) {
        super("Given login [" + login + "] does not exist.");
    }
}

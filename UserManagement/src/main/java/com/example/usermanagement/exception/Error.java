package com.example.usermanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {

    USER_NOT_FOUND("userNotFound", "User is not found in the database."),
    USER_ALREADY_EXISTS("userAlreadyExists", "There is a user with these credentials, please login.");

    private final String errorCode;
    private final String errorMessage;
}

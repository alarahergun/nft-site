package com.example.usermanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {

    USER_NOT_FOUND("userNotFound", "User is not found in the database.");

    private final String errorCode;
    private final String errorMessage;
}

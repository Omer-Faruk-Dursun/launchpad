package com.demo.launchpad.security;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNEXPECTED_SERVER_ERROR(1001, "Unexpected server error occurred"),
    HTTP_REQUEST_NOT_READABLE(1002, "Http Request is not readable"),
    VALIDATION_FAILED(1003, "Request validation failed, please check details"),
    USER_ALREADY_EXISTS(1011, "User already exists"),
    EMAIL_ALREADY_IN_USE(1012, "Email is already in use"),
    INVALID_CREDENTIALS(1013, "Invalid username or password"),
    USER_NOT_FOUND(1014, "User not found");

    private final Integer code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

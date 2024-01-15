package com.example.ouruniverse.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOTFOUND(HttpStatus.NOT_FOUND, "User Not Found" ),

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired Token" ),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Invalid Token");

    private final HttpStatus status;
    private final String message;
}

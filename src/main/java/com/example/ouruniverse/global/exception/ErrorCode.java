package com.example.ouruniverse.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    REFRESH_NOTFOUND(HttpStatus.BAD_REQUEST, "RefreshToken Not Found"),
    REFRESH_INVALID(HttpStatus.BAD_REQUEST, "RefreshToken Invalid"),

    USER_NOTFOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    ALREADY_SIGNUP(HttpStatus.BAD_REQUEST, "Already Signed up User"),

    IMG_EXCEPTION(HttpStatus.BAD_REQUEST, "Img Exception" ),

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired Token" ),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Invalid Token");

    private final HttpStatus status;
    private final String message;
}

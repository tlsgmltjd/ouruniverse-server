package com.example.ouruniverse.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HappyException extends RuntimeException {
    private final ErrorCode errorCode;
}

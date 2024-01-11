package com.example.ouruniverse.domain.auth.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoInfo {
    private KaKaoAccount kakaoAccount;

    public static KaKaoInfo fail() {
        return null;
    }
}
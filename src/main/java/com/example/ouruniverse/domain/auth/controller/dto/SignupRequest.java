package com.example.ouruniverse.domain.auth.controller.dto;

import com.example.ouruniverse.domain.user.entity.Grade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotNull
    private Grade grade;

    @NotBlank
    private String nickName;

    @NotNull
    private School school;
}

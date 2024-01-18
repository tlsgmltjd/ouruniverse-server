package com.example.ouruniverse.domain.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class School {

    @NotBlank
    private String schoolName;

    @NotBlank
    private String provincialOfficeOfEducationCode;

    @NotNull
    private Integer administrativeStandardCode;

    @NotBlank
    private String schoolPosition;

    @NotBlank
    private String establishmentName;
}

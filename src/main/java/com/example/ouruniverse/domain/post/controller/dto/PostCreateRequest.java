package com.example.ouruniverse.domain.post.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}

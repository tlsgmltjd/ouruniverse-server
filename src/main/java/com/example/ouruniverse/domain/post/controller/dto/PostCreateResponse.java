package com.example.ouruniverse.domain.post.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateResponse {
    private Long id;
    private String title;
    private String content;
    private List<String> imgUrls;
}

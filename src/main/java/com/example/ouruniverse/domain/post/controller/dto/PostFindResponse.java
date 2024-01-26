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
public class PostFindResponse {
    private List<PostResponseDto> response;
    private boolean last;
}

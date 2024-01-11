package com.example.ouruniverse.domain.auth.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Profile {
    private String nickname;
    private String thumbnail_image_url;
    private String profile_image_url;
    private String is_default_image;
}
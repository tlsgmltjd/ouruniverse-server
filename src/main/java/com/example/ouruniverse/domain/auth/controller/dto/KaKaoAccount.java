package com.example.ouruniverse.domain.auth.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KaKaoAccount {
    private Profile profile;
    private String email;
}

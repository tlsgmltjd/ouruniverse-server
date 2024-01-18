package com.example.ouruniverse.domain.user.controller.dto;

import com.example.ouruniverse.domain.user.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private Long userId;
    private String userName;
    private Grade grade;
    private String userEmail;
}

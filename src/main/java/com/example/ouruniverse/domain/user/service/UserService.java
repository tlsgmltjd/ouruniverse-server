package com.example.ouruniverse.domain.user.service;

import com.example.ouruniverse.domain.user.controller.dto.UserInfoResponse;
import com.example.ouruniverse.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserInfoResponse userFind(UserEntity user) {

        return UserInfoResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}

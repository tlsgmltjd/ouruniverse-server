package com.example.ouruniverse.domain.user.controller;

import com.example.ouruniverse.domain.user.controller.dto.UserInfoResponse;
import com.example.ouruniverse.domain.user.entity.UserEntity;
import com.example.ouruniverse.domain.user.service.UserService;
import com.example.ouruniverse.global.common.UserManager;
import com.example.ouruniverse.global.exception.ErrorCode;
import com.example.ouruniverse.global.exception.HappyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserManager userManager;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> findUser() {
        UserEntity user = userManager.getCurrentUser();

        return ResponseEntity.ok(userService.userFind(user));
    }
}

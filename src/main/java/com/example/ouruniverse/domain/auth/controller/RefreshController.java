package com.example.ouruniverse.domain.auth.controller;

import com.example.ouruniverse.domain.auth.service.RefreshTokenService;
import com.example.ouruniverse.global.common.ConstantsUtil;
import com.example.ouruniverse.global.common.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshTokenService refreshTokenService;
    private final CookieManager cookieManager;

    @GetMapping("/token/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieManager.getCookieValue(request, ConstantsUtil.refreshToken);
        refreshTokenService.refresh(refreshToken, response);
        return ResponseEntity.ok().build();
    }
}

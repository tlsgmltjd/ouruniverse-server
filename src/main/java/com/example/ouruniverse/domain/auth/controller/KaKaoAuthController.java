package com.example.ouruniverse.domain.auth.controller;

import com.example.ouruniverse.domain.auth.controller.dto.KaKaoAccount;
import com.example.ouruniverse.domain.auth.service.KaKaoAuthService;
import com.example.ouruniverse.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KaKaoAuthController {

    private final KaKaoAuthService kaKaoAuthService;

    @Value("${kakao.restapiKey}")
    private String restapiKey;

    @Value("${kakao.redirectUrl}")
    private String redirectUri;

    @GetMapping("/callback")
    public ResponseEntity<Void> getKakaoAccount(@RequestParam("code") String code, HttpServletResponse response) {
      KaKaoAccount kaKaoAccount = kaKaoAuthService.getInfo(code).getKakaoAccount();

      String accessToken = JwtProvider.createToken(kaKaoAccount.getEmail());
      String refreshToken = JwtProvider.createRefreshToken(kaKaoAccount.getEmail());

      response.addHeader(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.BEARER_PREFIX + accessToken);
      response.addHeader("Refresh-Token", JwtProvider.BEARER_PREFIX + refreshToken);

      return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<Void> KaKaoAuthLink() {
        String redirectUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?response_type=code&" +
                "client_id=" + restapiKey +
                "&redirect_uri=" + redirectUri;

        return ResponseEntity
                .status(302)
                .location(UriComponentsBuilder.fromHttpUrl(redirectUrl).build().toUri())
                .build();
    }
}

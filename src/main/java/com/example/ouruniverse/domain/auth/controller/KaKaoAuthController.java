package com.example.ouruniverse.domain.auth.controller;

import com.example.ouruniverse.domain.auth.controller.dto.KaKaoAccount;
import com.example.ouruniverse.domain.auth.service.KaKaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KaKaoAuthController {

    private final KaKaoAuthService kaKaoAuthService;


    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUri;

    @GetMapping("/callback")
    public ResponseEntity<KaKaoAccount> getKakaoAccount(@RequestParam("code") String code) {
      log.debug("code = {}", code);
      KaKaoAccount kaKaoAccount = kaKaoAuthService.getInfo(code).getKakaoAccount();
      return ResponseEntity.ok(kaKaoAccount);
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

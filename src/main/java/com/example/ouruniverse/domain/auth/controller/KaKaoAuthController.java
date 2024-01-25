package com.example.ouruniverse.domain.auth.controller;

import com.example.ouruniverse.domain.auth.controller.dto.KakaoLoginPageReponse;
import com.example.ouruniverse.domain.auth.service.KaKaoAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
@Slf4j
public class KaKaoAuthController {

    private final KaKaoAuthService kaKaoAuthService;

    @Value("${kakao.restapiKey}")
    private String restapiKey;

    @Value("${kakao.redirectUrl}")
    private String redirectUri;

    @GetMapping("/callback")
    public ResponseEntity<Void> getKakaoAccount(@RequestParam("code") String code) {
      kaKaoAuthService.getInfo(code);

      return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<KakaoLoginPageReponse> KaKaoAuthLink(HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?response_type=code&" +
                "client_id=" + restapiKey +
                "&redirect_uri=" + redirectUri;

        return ResponseEntity.ok(new KakaoLoginPageReponse(redirectUrl));
    }
}

package com.example.ouruniverse.domain.auth.service;

import com.example.ouruniverse.domain.auth.controller.dto.KaKaoInfo;
import com.example.ouruniverse.domain.auth.controller.dto.KaKaoToken;
import com.example.ouruniverse.global.feign.client.KaKaoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoAuthService {

    private final KaKaoClient client;

    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    public KaKaoInfo getInfo(final String code) {
        final KaKaoToken token = getToken(code);
        log.debug("token = {}", token);
        try {
            return client.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());
        } catch (Exception e) {
            log.error("something error..", e);
            return KaKaoInfo.fail();
        }
    }

    private KaKaoToken getToken(final String code) {
        try {
            return client.getToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something error..", e);
            return KaKaoToken.fail();
        }
    }
}

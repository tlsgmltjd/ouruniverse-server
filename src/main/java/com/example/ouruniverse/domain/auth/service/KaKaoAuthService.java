package com.example.ouruniverse.domain.auth.service;

import com.example.ouruniverse.domain.auth.controller.dto.*;
import com.example.ouruniverse.domain.user.entity.UserEntity;
import com.example.ouruniverse.domain.user.repository.UserRepository;
import com.example.ouruniverse.global.common.ConstantsUtil;
import com.example.ouruniverse.global.common.CookieManager;
import com.example.ouruniverse.global.common.UserManager;
import com.example.ouruniverse.global.feign.client.KaKaoClient;
import com.example.ouruniverse.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoAuthService {

    private final KaKaoClient client;
    private final CookieManager cookieManager;
    private final HttpServletResponse httpServletResponse;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Value("${kakao.authUrl}")
    private String kakaoAuthUrl;

    @Value("${kakao.userApiUrl}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapiKey}")
    private String restapiKey;

    @Value("${kakao.redirectUrl}")
    private String redirectUrl;

    @Transactional
    public void getInfo(final String code) {
        final KaKaoToken token = getToken(code);
        try {
            KaKaoInfo kaKaoInfo = client.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());

            String accessToken = JwtProvider.createToken(kaKaoInfo.getKakaoAccount().getEmail());
            String refreshToken = JwtProvider.createRefreshToken(kaKaoInfo.getKakaoAccount().getEmail());

            if (!userRepository.existsByEmail(kaKaoInfo.getKakaoAccount().getEmail())) {
                UserEntity user = UserEntity.builder()
                        .name(kaKaoInfo.getKakaoAccount().getProfile().getNickname())
                        .email(kaKaoInfo.getKakaoAccount().getEmail())
                        .profileUrl(kaKaoInfo.getKakaoAccount().getProfile().getProfile_image_url())
                        .build();

                userRepository.save(user);
            }

            refreshTokenService.saveRefreshToken(refreshToken, userRepository.findByEmail(kaKaoInfo.getKakaoAccount().getEmail())
                    .orElseThrow(RuntimeException::new).getId());

            cookieManager.addTokenCookie(httpServletResponse, ConstantsUtil.accessToken,  accessToken, JwtProvider.TOKEN_TIME, true);
            cookieManager.addTokenCookie(httpServletResponse, ConstantsUtil.refreshToken, refreshToken, JwtProvider.REFRESH_TOKEN_TIME, true);

        } catch (Exception e) {
            log.error("something error..", e);
            throw new RuntimeException();
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

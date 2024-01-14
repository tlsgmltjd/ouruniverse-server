package com.example.ouruniverse.domain.auth.service;

import com.example.ouruniverse.domain.auth.controller.dto.IsSignupResponse;
import com.example.ouruniverse.domain.auth.controller.dto.KaKaoInfo;
import com.example.ouruniverse.domain.auth.controller.dto.KaKaoToken;
import com.example.ouruniverse.domain.user.entity.UserEntity;
import com.example.ouruniverse.domain.user.repository.UserRepository;
import com.example.ouruniverse.global.common.ConstantsUtil;
import com.example.ouruniverse.global.common.CookieManager;
import com.example.ouruniverse.global.feign.client.KaKaoClient;
import com.example.ouruniverse.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    public boolean getInfo(final String code) {
        boolean isSignup;

        final KaKaoToken token = getToken(code);
        try {
            KaKaoInfo kaKaoInfo = client.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());

            String accessToken = JwtProvider.createToken(kaKaoInfo.getKakaoAccount().getEmail());
            String refreshToken = JwtProvider.createRefreshToken(kaKaoInfo.getKakaoAccount().getEmail());

            if (!userRepository.existsByEmail(kaKaoInfo.getKakaoAccount().getEmail())) {
                UserEntity user = UserEntity.builder()
                        .name(kaKaoInfo.getKakaoAccount().getProfile().getNickname())
                        .email(kaKaoInfo.getKakaoAccount().getEmail())
                        .follows(List.of())
                        .followings(List.of())
                        .build();

                userRepository.save(user);

                isSignup = false;
            } else {
                UserEntity user = userRepository.findByEmail(kaKaoInfo.getKakaoAccount().getEmail())
                        .orElseThrow(RuntimeException::new);

                isSignup = user.getGrade() != null && user.getSchoolId() != null;
            }

            refreshTokenService.saveRefreshToken(refreshToken, userRepository.findByEmail(kaKaoInfo.getKakaoAccount().getEmail())
                    .orElseThrow(RuntimeException::new).getId());

            cookieManager.addTokenCookie(httpServletResponse, ConstantsUtil.accessToken,  accessToken, JwtProvider.TOKEN_TIME, true);
            cookieManager.addTokenCookie(httpServletResponse, ConstantsUtil.refreshToken, refreshToken, JwtProvider.REFRESH_TOKEN_TIME, true);

            return isSignup;
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

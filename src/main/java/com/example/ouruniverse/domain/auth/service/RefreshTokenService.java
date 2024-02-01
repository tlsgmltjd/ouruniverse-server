package com.example.ouruniverse.domain.auth.service;

import com.example.ouruniverse.domain.auth.entity.RefreshTokenEntity;
import com.example.ouruniverse.domain.auth.repository.RefreshTokenRepository;
import com.example.ouruniverse.domain.user.entity.UserEntity;
import com.example.ouruniverse.domain.user.repository.UserRepository;
import com.example.ouruniverse.global.common.ConstantsUtil;
import com.example.ouruniverse.global.common.CookieManager;
import com.example.ouruniverse.global.exception.ErrorCode;
import com.example.ouruniverse.global.exception.HappyException;
import com.example.ouruniverse.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.ouruniverse.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;

    @Transactional
    public void saveRefreshToken(String token, Long userId) {

        if (refreshTokenRepository.existsByUserId(userId))
            refreshTokenRepository.deleteByUserId(userId);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .refreshToken(token)
                .userId(userId)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null)
            throw new HappyException(REFRESH_NOTFOUND);

        String email = jwtProvider.getUserInfoFromToken(refreshToken).getSubject();

        UserEntity findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new HappyException(USER_NOTFOUND));
        RefreshTokenEntity findRefreshToken = refreshTokenRepository.findByUserId(findUser.getId())
                .orElseThrow(() -> new HappyException(REFRESH_NOTFOUND));


        String newAccessToken = JwtProvider.createToken(findUser.getEmail());
        String newRefreshToken = JwtProvider.createRefreshToken(findUser.getEmail());

        if (!findUser.getId().equals(findRefreshToken.getUserId()) || !findRefreshToken.getRefreshToken().equals(refreshToken) && jwtProvider.isValidToken(refreshToken)) {
            throw new HappyException(REFRESH_INVALID);
        }

        cookieManager.addTokenCookie(response, ConstantsUtil.accessToken, newAccessToken, JwtProvider.TOKEN_TIME, true);
        cookieManager.addTokenCookie(response, ConstantsUtil.refreshToken, newRefreshToken, JwtProvider.REFRESH_TOKEN_TIME, true);

        RefreshTokenEntity updatedRefreshToken = findRefreshToken.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(updatedRefreshToken);
    }
}

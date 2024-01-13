package com.example.ouruniverse.domain.auth.service;

import com.example.ouruniverse.domain.auth.entity.RefreshTokenEntity;
import com.example.ouruniverse.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

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
}

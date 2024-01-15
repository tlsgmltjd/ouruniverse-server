package com.example.ouruniverse.domain.auth.repository;

import com.example.ouruniverse.domain.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refrshToken);
    Optional<RefreshTokenEntity> findByUserId(Long userId);
    void deleteByRefreshToken(String refreshToken);
    void deleteByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

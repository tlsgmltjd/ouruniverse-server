package com.example.ouruniverse.global.security.jwt.repository;

import com.example.ouruniverse.global.security.jwt.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}

package com.example.ouruniverse.domain.user.repository;

import com.example.ouruniverse.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

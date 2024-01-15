package com.example.ouruniverse.global.common;

import com.example.ouruniverse.domain.user.entity.UserEntity;
import com.example.ouruniverse.domain.user.repository.UserRepository;
import com.example.ouruniverse.global.exception.ErrorCode;
import com.example.ouruniverse.global.exception.HappyException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.example.ouruniverse.global.exception.ErrorCode.USER_NOTFOUND;

@Component
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new HappyException(USER_NOTFOUND));
    }
}

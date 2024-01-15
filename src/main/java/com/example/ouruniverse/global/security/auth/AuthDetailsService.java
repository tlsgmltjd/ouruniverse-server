package com.example.ouruniverse.global.security.auth;

import com.example.ouruniverse.domain.user.repository.UserRepository;
import com.example.ouruniverse.global.exception.ErrorCode;
import com.example.ouruniverse.global.exception.HappyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.ouruniverse.global.exception.ErrorCode.USER_NOTFOUND;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(AuthDetails::new)
                .orElseThrow(() -> new HappyException(USER_NOTFOUND));
    }
}

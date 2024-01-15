package com.example.ouruniverse.global.security.jwt;

import com.example.ouruniverse.global.exception.ErrorCode;
import com.example.ouruniverse.global.exception.HappyException;
import com.example.ouruniverse.global.security.auth.AuthDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.example.ouruniverse.global.exception.ErrorCode.EXPIRED_TOKEN;
import static com.example.ouruniverse.global.exception.ErrorCode.INVALID_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long TOKEN_TIME = 60 * 60 * 1000L;
    public static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final AuthDetailsService authDetailsService;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public static String createToken(String email) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public static String createRefreshToken(String email) {
        Date date = new Date();

        return Jwts.builder()
                .setIssuedAt(date)
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public UsernamePasswordAuthenticationToken authentication(String token) {
        UserDetails userDetails = authDetailsService.loadUserByUsername(getUserInfoFromToken(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Claims getUserInfoFromToken(String token) {
        try {
            if (token == null || token.isEmpty())
                throw new HappyException(INVALID_TOKEN);

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new HappyException(EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new HappyException(INVALID_TOKEN);
        }
    }
}
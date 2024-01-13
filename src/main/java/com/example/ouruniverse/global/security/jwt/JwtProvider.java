package com.example.ouruniverse.global.security.jwt;

import com.example.ouruniverse.global.security.auth.AuthDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public UsernamePasswordAuthenticationToken authentication(String token){
        UserDetails userDetails = authDetailsService.loadUserByUsername(getUserInfoFromToken(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } catch (Exception e) {
            log.error("=========== 토큰 검증 중 예외가 발생 했습니다. ===========");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
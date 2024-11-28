package com.example.demo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {

    // 비밀 키를 생성하거나, 외부에서 주입받을 수 있도록 설정
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 512비트 키 자동 생성
//    @Value("${jwt.secret}")
//    private String secretKey;  // 비밀키를 외부에서 읽어오는 경우 주석을 해제하고 사용

    /**
     * 사용자 정보를 기반으로 JWT 생성
     * 
     * @param userEntity 사용자 엔티티
     * @return 생성된 JWT 토큰
     */
    public String create(UserEntity userEntity) {
        // 기한을 지금으로부터 1일로 설정
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        
        // JWT Token 생성
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)  // 서명 키로 SECRET_KEY 사용
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    /**
     * JWT를 검증하고 사용자 ID 반환
     * 
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)  // 검증할 때도 동일한 SECRET_KEY 사용
                    .parseClaimsJws(token)     // JWT 파싱 
                    .getBody();

            // 만료된 토큰일 경우 처리
            if (claims.getExpiration().before(new Date())) {
                throw new IllegalArgumentException("Token has expired");
            }

            return claims.getSubject(); // 사용자 ID 반환
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature", e);  // 서명 불일치
        } catch (Exception e) {
            throw new RuntimeException("JWT parsing error", e);  // 기타 예외 처리
        }
    }
}

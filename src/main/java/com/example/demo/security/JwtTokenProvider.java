//package com.example.demo.security;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Base64.Decoder;
//import java.util.Collection;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import com.example.demo.dto.JwtToken;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts; // 수정된 부분
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.UnsupportedJwtException;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class JwtTokenProvider {
//   private final Key key;
//
//    // properties에서 secret값 가져와서 key에 저장
//    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
//        byte[] keyBytes = Decoder.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//     // 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
//    public JwtToken generateToken(Authentication authentication) {
//       //  권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//        long now = (new Date()).getTime();
//
//         //Access Token 생성
//        Date accessTokenExpiresIn = new Date(now + 86400000);
//        String accessToken = Jwts.builder() // 수정된 부분
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(SignatureAlgorithm.HS256, key)
//                .compact();
//
//        // Refresh Token 생성
//        String refreshToken = Jwts.builder() // 수정된 부분
//                .setExpiration(new Date(now + 86400000))
//                .signWith(SignatureAlgorithm.HS256, key)
//                .compact();
//
//        return JwtToken.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//     //Jwt토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
//    public Authentication getAuthentication(String accessToken) {
//        // Jwt 토큰 복호화
//        Claims claims = parseClaims(accessToken);
//
//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        // 클레임에서 권한 정보 가져오기
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get("auth").toString().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        // UserDetails 객체를 만들어서 Authentication return
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//
//    // 토큰 정보를 검증하는 메서드
//    public boolean validateToken(String token) {
//        try {
//            Jwts.builder()
//            .setSigningKey(key)
//            .build()
//            .parseClaimsJws(token);
//            return true;
//        } catch (SecurityException | JwtException e) { // 수정된 부분
//            log.info("Invalid JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty", e);
//        }
//        return false;
//    }
//
//    // accessToken
//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.builder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
//}

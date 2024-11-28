package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
	private TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    try {
	        // 요청에서 토큰 가져오기
	        String token = parseBearerToken(request);
	        log.info("Filter is running...");

	        if (token != null && !token.equalsIgnoreCase("null")) {
	            // userId 가져오기
	            String userId = tokenProvider.validateAndGetUserId(token);
	            log.info("Authenticated user ID: " + userId);

	            if (userId != null) {
	                // 인증 객체 생성
	                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                        userId,  // userId가 null이 아닌지 확인
	                        null,    // 패스워드가 없으므로 null
	                        AuthorityUtils.NO_AUTHORITIES
	                );
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                
	                // SecurityContext에 인증 객체 설정
	                SecurityContext securityContext = SecurityContextHolder.getContext(); // 기존 SecurityContext 가져오기
	                securityContext.setAuthentication(authentication);
	                SecurityContextHolder.setContext(securityContext);
	                log.info("User authentication set in security context.");
	            } else {
	                log.error("User ID is null. Cannot authenticate.");
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
	            }
	        }
	    } catch (SignatureException ex) {
	        logger.error("Invalid JWT signature", ex);
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature");
	    } catch (ExpiredJwtException ex) {
	        logger.error("Expired JWT token", ex);
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token");
	    } catch (JwtException ex) {
	        logger.error("JWT parsing error", ex);
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT parsing error");
	    } catch (Exception ex) {
	        logger.error("Could not set user authentication in security context", ex);
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
	    }

	    filterChain.doFilter(request, response);
	}

	private String parseBearerToken(HttpServletRequest request) {
		// Http 요청의 헤더를 파싱해 Bearer 토근을 리턴한다
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}

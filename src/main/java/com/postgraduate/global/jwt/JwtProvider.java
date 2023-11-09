package com.postgraduate.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.user.application.exception.NotFoundUserException;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.auth.AuthDetailsService;
import com.postgraduate.global.config.redis.RedisRepository;
import com.postgraduate.global.dto.ResponseDto;
import com.postgraduate.global.exception.ApplicationException;
import com.postgraduate.global.jwt.exception.InvalidRefreshTokenException;
import com.postgraduate.global.jwt.exception.InvalidTokenException;
import com.postgraduate.global.jwt.exception.NoneRefreshTokenException;
import com.postgraduate.global.jwt.exception.TokenExpiredException;
import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.logging.service.LogService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final AuthDetailsService authDetailsService;
    private final RedisRepository redisRepository;
    private final LogService logService;

    @Value("${jwt.secret-key}")
    private String secret;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;
    private final String ROLE = "role";
    private final String REFRESH = "refresh";
    private final String AUTHORIZATION = "Authorization";

    public String generateAccessToken(Long id, Role role) {
        Instant accessDate = LocalDateTime.now().plusSeconds(accessExpiration).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .claim(ROLE, role)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(accessDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(Long id, Role role) {
        Instant refreshDate = LocalDateTime.now().plusSeconds(refreshExpiration).atZone(ZoneId.systemDefault()).toInstant();
        String refreshToken = Jwts.builder()
                .claim(ROLE, role)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(refreshDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        redisRepository.setValues(REFRESH + id, refreshToken, Duration.ofSeconds(refreshExpiration));
        return refreshToken;
    }

    public Authentication getAuthentication(HttpServletResponse response, String token) throws NotFoundUserException{
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get(ROLE).toString()));
        return new UsernamePasswordAuthenticationToken(getDetails(response, claims), "", authorities);
    }

    private AuthDetails getDetails(HttpServletResponse response, Claims claims) {
        try {
            AuthDetails authDetails = authDetailsService.loadUserByUsername(claims.getSubject());
            return authDetails;
        } catch (NotFoundUserException ex) {
            jwtExceptionHandler(response, ex);
            throw ex;
        }
    }

    public boolean validateToken(HttpServletResponse response, String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            jwtExceptionHandler(response, new InvalidTokenException());
            return false;
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response, new TokenExpiredException());
            return false;
        }
    }

    public void checkRedis(Long id, HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTHORIZATION).split(" ")[1];
        String redisToken = redisRepository.getValues(REFRESH + id)
                .orElseThrow(NoneRefreshTokenException::new);
        if (!redisToken.equals(refreshToken))
            throw new InvalidRefreshTokenException();
    }

    public Claims parseClaims(String token) {
        JwtParser parser = Jwts.parser().setSigningKey(secret);
        return parser.parseClaimsJws(token).getBody();
    }

    private void jwtExceptionHandler(HttpServletResponse response, ApplicationException ex) {
        response.setStatus(500);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            logService.save(new LogRequest(ex.getMessage()));
            String json = new ObjectMapper().writeValueAsString(ResponseDto.create(ex.getErrorCode(), ex.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

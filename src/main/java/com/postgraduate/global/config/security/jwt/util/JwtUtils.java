package com.postgraduate.global.config.security.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.user.user.exception.UserNotFoundException;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.entity.constant.Role;
import com.postgraduate.global.config.security.jwt.auth.AuthDetails;
import com.postgraduate.global.config.security.jwt.auth.AuthDetailsService;
import com.postgraduate.global.config.security.jwt.constant.Type;
import com.postgraduate.global.config.security.jwt.exception.InvalidRefreshTokenException;
import com.postgraduate.global.config.security.jwt.exception.InvalidTokenException;
import com.postgraduate.global.config.redis.RedisRepository;
import com.postgraduate.global.config.security.jwt.filter.JwtFilter;
import com.postgraduate.global.dto.ResponseDto;
import com.postgraduate.global.exception.ApplicationException;
import com.postgraduate.global.config.security.jwt.exception.NoneRefreshTokenException;
import com.postgraduate.global.config.security.jwt.exception.TokenExpiredException;
import com.postgraduate.global.aop.logging.dto.LogRequest;
import com.postgraduate.global.aop.logging.service.LogService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.postgraduate.global.config.security.jwt.constant.Type.*;
import static org.springframework.http.HttpStatus.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    private final AuthDetailsService authDetailsService;
    private final RedisRepository redisRepository;
    private final LogService logService;

    @Value("${jwt.secret-key}")
    private String secret;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;
    @Value("${log.Type}")
    private String env;

    private static final String ROLE = "role";
    private static final String TYPE = "type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    public String generateAccessToken(Long id, Role role) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        Instant accessDate = LocalDateTime.now().plusSeconds(accessExpiration).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .claim(ROLE, role)
                .claim(TYPE, ACCESS)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(accessDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long id, Role role) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        Instant refreshDate = LocalDateTime.now().plusSeconds(refreshExpiration).atZone(ZoneId.systemDefault()).toInstant();
        String refreshToken = Jwts.builder()
                .claim(ROLE, role)
                .claim(TYPE, REFRESH)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(refreshDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        redisRepository.setValues(REFRESH.toString() + id, refreshToken, Duration.ofSeconds(refreshExpiration));
        return refreshToken;
    }

    /**
     * 중복 요청 임시 해결용 함수
     */
    public String checkPast(Long id, HttpServletRequest request) {
        String redisToken = redisRepository.getValues(REFRESH.toString() + id)
                .orElseThrow(NoneRefreshTokenException::new);

        String refreshToken = request.getHeader(AUTHORIZATION).split(" ")[1];
        String pastToken = redisRepository.getValues(PAST_REFRESH.toString() + id)
                .orElse(null);
        if (pastToken != null && pastToken.equals(refreshToken)) {
            return getClaim(refreshToken);
        }

        return checkRedis(refreshToken, redisToken, id);
    }

    private String checkRedis(String refreshToken, String redisToken, Long id) {
        if (!redisToken.equals(refreshToken))
            throw new InvalidRefreshTokenException();
        redisRepository.setValues(PAST_REFRESH.toString() + id, redisToken, Duration.ofSeconds(3));
        return getClaim(refreshToken);
    }

    private String getClaim(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        return claims.get(ROLE).toString();
    }

    public String originCheckRedis(Long id, HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTHORIZATION).split(" ")[1];
        String redisToken = redisRepository.getValues(REFRESH.toString() + id)
                .orElseThrow(NoneRefreshTokenException::new);
        if (!redisToken.equals(refreshToken))
            throw new InvalidRefreshTokenException();
        Claims claims = parseClaims(refreshToken);
        return claims.get(ROLE).toString();
    }

    public void makeExpired(Long id) {
        redisRepository.deleteValues(REFRESH.toString() + id);
    }

    public Authentication getAuthentication(HttpServletResponse response, String token) throws UserNotFoundException {
        Claims claims = parseClaims(token);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get(ROLE).toString()));
        User user = getDetails(response, claims).getUser();
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    private AuthDetails getDetails(HttpServletResponse response, Claims claims) {
        try {
            return authDetailsService.loadUserByUsername(claims.getSubject());
        } catch (UserNotFoundException ex) {
            jwtExceptionHandler(BAD_REQUEST, response, ex);
            throw ex;
        }
    }

    public boolean validateToken(HttpServletResponse response, String token, Type type) {
        try {
            Claims claims = parseClaims(token);
            if (!claims.get(TYPE).equals(type.name()))
                throw new InvalidTokenException();
            return true;
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(OK, response, new TokenExpiredException());
            throw new TokenExpiredException();
        } catch (Exception e) {
            jwtExceptionHandler(OK, response, new InvalidTokenException());
            throw new InvalidTokenException();
        }
    }

    private Claims parseClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void jwtExceptionHandler(HttpStatus status, HttpServletResponse response, ApplicationException ex) {
        response.setStatus(status.value());
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        log.error("errorCode {}, errorMessage {}", ex.getCode(), ex.getMessage());
        try {
            logService.save(new LogRequest(env, JwtFilter.class.getSimpleName(), ex.getMessage()));
            String json = new ObjectMapper().writeValueAsString(ResponseDto.create(ex.getCode(), ex.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}


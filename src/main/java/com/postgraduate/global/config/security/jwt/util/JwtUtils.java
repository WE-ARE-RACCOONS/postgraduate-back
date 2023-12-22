package com.postgraduate.global.config.security.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.auth.AuthDetailsService;
import com.postgraduate.global.config.security.jwt.constant.Type;
import com.postgraduate.global.config.security.jwt.exception.InvalidRefreshTokenException;
import com.postgraduate.global.config.security.jwt.exception.InvalidTokenException;
import com.postgraduate.global.config.redis.RedisRepository;
import com.postgraduate.global.dto.ResponseDto;
import com.postgraduate.global.exception.ApplicationException;
import com.postgraduate.global.config.security.jwt.exception.NoneRefreshTokenException;
import com.postgraduate.global.config.security.jwt.exception.TokenExpiredException;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.postgraduate.global.config.security.jwt.constant.Type.ACCESS;
import static com.postgraduate.global.config.security.jwt.constant.Type.REFRESH;

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
    private static final String ROLE = "role";
    private static final String TYPE = "type";
    private static final String AUTHORIZATION = "Authorization";
    private static final int STATUS = 500;
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    public String generateAccessToken(Long id, Role role) {
        Instant accessDate = LocalDateTime.now().plusSeconds(accessExpiration).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .claim(ROLE, role)
                .claim(TYPE, ACCESS)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(accessDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(Long id, Role role) {
        Instant refreshDate = LocalDateTime.now().plusSeconds(refreshExpiration).atZone(ZoneId.systemDefault()).toInstant();
        String refreshToken = Jwts.builder()
                .claim(ROLE, role)
                .claim(TYPE, REFRESH)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(refreshDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        redisRepository.setValues(REFRESH.toString() + id, refreshToken, Duration.ofSeconds(refreshExpiration));
        return refreshToken;
    }

    public String checkRedis(Long id, HttpServletRequest request) {
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
            AuthDetails authDetails = authDetailsService.loadUserByUsername(claims.getSubject());
            return authDetails;
        } catch (UserNotFoundException ex) {
            jwtExceptionHandler(response, ex);
            throw ex;
        }
    }

    public boolean validateToken(HttpServletResponse response, String token, Type type) {
        try {
            Claims claims = parseClaims(token);
            if (!claims.get(TYPE).equals(type.name()))
                throw new InvalidTokenException();
            return true;
        } catch (ApplicationException | SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            jwtExceptionHandler(response, new InvalidTokenException());
            return false;
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response, new TokenExpiredException());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        JwtParser parser = Jwts.parser().setSigningKey(secret);
        return parser.parseClaimsJws(token).getBody();
    }

    private void jwtExceptionHandler(HttpServletResponse response, ApplicationException ex) {
        response.setStatus(STATUS);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        try {
            logService.save(new LogRequest(ex.getMessage()));
            String json = new ObjectMapper().writeValueAsString(ResponseDto.create(ex.getErrorCode(), ex.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

package com.postgraduate.global.jwt;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.auth.AuthDetailsService;
import com.postgraduate.global.config.redis.RedisRepository;
import com.postgraduate.global.jwt.exception.InvalidRefreshTokenException;
import com.postgraduate.global.jwt.exception.InvalidTokenException;
import com.postgraduate.global.jwt.exception.NoneRefreshTokenException;
import com.postgraduate.global.jwt.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class JwtProvider {
    private final AuthDetailsService authDetailsService;
    private final RedisRepository redisRepository;
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

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get(ROLE).toString()));
        return new UsernamePasswordAuthenticationToken(getDetails(claims), "", authorities);
    }

    private AuthDetails getDetails(Claims claims) {
        return this.authDetailsService.loadUserByUsername(claims.getSubject());
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
    }

    public void checkRedis(Long id, HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTHORIZATION).split(" ")[1];
        String redisToken = redisRepository.getValues(REFRESH + id).orElseThrow(NoneRefreshTokenException::new);
        if (!redisToken.equals(refreshToken))
            throw new InvalidRefreshTokenException();
    }

    public Claims parseClaims(String token) {
        JwtParser parser = Jwts.parser().setSigningKey(secret);
        return parser.parseClaimsJws(token).getBody();
    }
}

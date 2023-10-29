package com.postgraduate.global.jwt;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.auth.AuthDetailsService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtProvider {
    private final String secret;
    private final AuthDetailsService authDetailsService;

    @Autowired
    public JwtProvider(@Value("${jwt.secret-key}") String secret, AuthDetailsService authDetailsService) {
        this.secret = secret;
        this.authDetailsService = authDetailsService;
    }

    public String generateToken(Long id, Role role, boolean isRefreshToken) {
        Instant accessDate = LocalDateTime.now().plusHours(6).atZone(ZoneId.systemDefault()).toInstant();
        Instant refreshDate = LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant();

        return Jwts.builder()
                .claim("role", role)
                .setSubject(String.valueOf(id))
                .setExpiration(isRefreshToken ? Date.from(refreshDate) : Date.from(accessDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").toString()));
        return new UsernamePasswordAuthenticationToken(getDetails(claims), "", authorities);
    }

    private AuthDetails getDetails(Claims claims) {
        if (claims.get("role").equals(Role.USER)) {
            return this.authDetailsService.loadUserByUsername(claims.getSubject());
        }
        return this.authDetailsService.loadUserByUsername(claims.getSubject());
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            //TODO: 유효하지 않은 토큰 예외
        } catch (ExpiredJwtException e) {
            //TODO: 만료된 토큰 예외
        }
    }

    public Claims parseClaims(String token) {
        try {
            JwtParser parser = Jwts.parser().setSigningKey(secret);
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

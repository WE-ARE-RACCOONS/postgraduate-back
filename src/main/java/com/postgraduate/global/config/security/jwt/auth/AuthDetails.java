package com.postgraduate.global.config.security.jwt.auth;

import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import static com.postgraduate.domain.member.user.domain.entity.constant.Role.*;

@Getter
@RequiredArgsConstructor
public class AuthDetails implements UserDetails {
    private final transient User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.isAdmin())
            return Collections.singletonList(new SimpleGrantedAuthority(ADMIN.name()));
        if (user.isSenior())
            return Collections.singletonList(new SimpleGrantedAuthority(SENIOR.name()));
        return Collections.singletonList(new SimpleGrantedAuthority(USER.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

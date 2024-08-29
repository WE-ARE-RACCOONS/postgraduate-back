package com.postgraduate.global.config.security.jwt.auth;

import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AuthDetails loadUserByUsername(String id) {
        User user = userRepository
                .findById(Long.valueOf(id))
                .orElseThrow(UserNotFoundException::new);
        return new AuthDetails(user);
    }
}

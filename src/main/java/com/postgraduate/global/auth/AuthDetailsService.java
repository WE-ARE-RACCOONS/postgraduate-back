package com.postgraduate.global.auth;

import com.postgraduate.domain.user.application.exception.NotFoundUserException;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AuthDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = this.userRepository
                .findById(Long.valueOf(id))
                .orElseThrow(NotFoundUserException::new);
        return new AuthDetails(user.getUserId(), user.getRole());
    }
}

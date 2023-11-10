package com.postgraduate.global.config.security.util;

import com.postgraduate.domain.user.application.exception.NotFoundUserException;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;
    public User getLoggedInUser(AuthDetails authDetails) {
        return userRepository.findById(authDetails.getUserId()).orElseThrow(NotFoundUserException::new);
    }

}
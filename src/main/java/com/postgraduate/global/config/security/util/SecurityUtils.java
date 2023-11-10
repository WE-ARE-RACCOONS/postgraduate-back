package com.postgraduate.global.config.security.util;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    public User getLoggedInUser(AuthDetails authDetails) {
        return authDetails.getUser();
    }

}
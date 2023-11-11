package com.postgraduate.domain.auth.application.dto.res;

import com.postgraduate.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
@AllArgsConstructor
public class AuthUserResponse {
    private Optional<User> user;
    private Long socialId;
}
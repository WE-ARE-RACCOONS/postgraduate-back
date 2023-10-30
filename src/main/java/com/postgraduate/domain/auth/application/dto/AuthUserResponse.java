package com.postgraduate.domain.auth.application.dto;

import com.postgraduate.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponse {
    private User user;
    private Long socialId;
}
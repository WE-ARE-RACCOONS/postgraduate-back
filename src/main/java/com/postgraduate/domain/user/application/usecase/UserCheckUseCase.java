package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCheckUseCase {
    private final UserGetService userGetService;
    public boolean isDuplicatedNickName(String nickName) {
        Optional<User> user = userGetService.byNickName(nickName);
        return user.isPresent();
    }
}

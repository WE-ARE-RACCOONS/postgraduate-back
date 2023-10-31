package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository userRepository;

    public void updateNickName(Long userId, String nickName) {
        User user = userRepository.findById(userId).get();
        user.updateNickName(nickName);
    }
}

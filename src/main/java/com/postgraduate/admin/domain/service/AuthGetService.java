package com.postgraduate.admin.domain.service;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthGetService {
    private final UserRepository userRepository;

    public User login(String nickName, String phoneNumber) {
        return userRepository.findByNickNameAndPhoneNumberAndIsDeleteIsFalse(nickName, phoneNumber)
                .orElseThrow();
    }

}

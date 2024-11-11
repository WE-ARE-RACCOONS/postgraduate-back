package com.postgraduate.domain.member.user.domain.service;

import com.postgraduate.domain.member.user.domain.repository.UserRepository;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSaveService {
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }
}

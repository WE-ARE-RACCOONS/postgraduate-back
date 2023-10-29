package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSaveService {
    private final UserRepository userRepository;

    public User saveUser(Long socialId) {
        User user = UserMapper.mapToUser(socialId);
        return userRepository.save(user);
    }
}

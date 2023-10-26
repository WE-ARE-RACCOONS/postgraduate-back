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
        Optional<User> user = userRepository.findBySocialId(socialId);

        if (user.isPresent()) {
            return user.get();
        }
        return userRepository.save(UserMapper.mapToUser(socialId));
    }
}

package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGetService {
    private final UserRepository userRepository;

    public User byUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User bySocialId(Long socialId) {
        return userRepository.findBySocialId(socialId).orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> byNickName(String nickName) {
        return userRepository.findByNickName(nickName);
    }

    public List<User> all() {
        return userRepository.findAll();
    }
}

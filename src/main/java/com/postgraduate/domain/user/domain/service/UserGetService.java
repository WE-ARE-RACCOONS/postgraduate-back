package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGetService {
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(/**예외 처리**/);
        return user;
    }

    public Optional<User> bySocialId(Long socialId) {
        return userRepository.findBySocialId(socialId);
    }

    public Optional<User> byNickName(String nickName) {
        return userRepository.findByNickName(nickName);
    }
    public List<User> all() {
        return userRepository.findAll();
    }
}

package com.postgraduate.domain.member.user.domain.service;

import com.postgraduate.domain.member.user.domain.entity.Wish;
import com.postgraduate.domain.member.user.domain.repository.UserRepository;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSaveService {
    private final UserRepository userRepository;

    public void saveSenior(User user) {
        userRepository.save(user);
    }

    public void saveJunior(User user, Wish wish) {
        user.addWish(wish);
        userRepository.saveJunior(user, wish);
    }

    public void changeJunior(User user, Wish wish) {
        user.addWish(wish);
        userRepository.changeJunior(wish);
    }
}

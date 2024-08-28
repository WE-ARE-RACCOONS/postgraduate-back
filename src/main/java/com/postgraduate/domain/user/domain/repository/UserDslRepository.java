package com.postgraduate.domain.user.domain.repository;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.Wish;

public interface UserDslRepository {
    void saveJunior(User user, Wish wish);

    void changeJunior(Wish wish);
    void deleteWish(User user);
}

package com.postgraduate.domain.user.wish.domain.repository;

import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long>, WishDslRepository {
    Optional<Wish> findByUserAndUser_IsDeleteIsFalse(User user);
    Optional<Wish> findByMatchingReceiveIsTrueAndUser_UserIdAndUser_IsDeleteIsFalse(Long userId);
    Optional<Wish> findByWishIdAndMatchingReceiveIsTrueAndUser_IsDeleteIsFalse(Long wishId);
    void deleteByUser(User user);
}

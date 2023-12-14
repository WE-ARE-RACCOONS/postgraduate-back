package com.postgraduate.domain.wish.domain.repository;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByUser(User user);
    List<Wish> findAllByUser_IsDelete(boolean isDelete);
}

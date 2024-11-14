package com.postgraduate.domain.wish.domain.repository;

import com.postgraduate.domain.wish.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
}

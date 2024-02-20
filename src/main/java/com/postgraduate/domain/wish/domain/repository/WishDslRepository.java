package com.postgraduate.domain.wish.domain.repository;

import com.postgraduate.domain.wish.domain.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishDslRepository {
    Page<Wish> findAllBySearchWish(String search, Pageable pageable);
}

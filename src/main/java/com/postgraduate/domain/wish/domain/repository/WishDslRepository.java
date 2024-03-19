package com.postgraduate.domain.wish.domain.repository;

import com.postgraduate.domain.wish.domain.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishDslRepository {
    Page<Wish> findAllBySearchWish(String search, Pageable pageable);
    List<Wish> findAllWish();
}

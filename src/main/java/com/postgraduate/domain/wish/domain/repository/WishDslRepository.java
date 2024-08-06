package com.postgraduate.domain.wish.domain.repository;

import com.postgraduate.domain.wish.domain.entity.Wish;

import java.util.List;

public interface WishDslRepository {
    List<Wish> findAllWish();
}

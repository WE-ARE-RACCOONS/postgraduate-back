package com.postgraduate.domain.user.wish.domain.repository;

import com.postgraduate.domain.user.wish.domain.entity.Wish;

import java.util.List;

public interface WishDslRepository {
    List<Wish> findAllWish();
}

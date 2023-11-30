package com.postgraduate.domain.wish.application.usecase;

import com.postgraduate.domain.wish.application.mapper.WishMapper;
import com.postgraduate.domain.wish.application.mapper.dto.res.WishResponse;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishInfoUseCase {
    private final WishGetService wishGetService;

    public WishResponse getWish(Long wishId) {
        Wish wish = wishGetService.byWishId(wishId);
        return WishMapper.mapToWish(wish);
    }
}

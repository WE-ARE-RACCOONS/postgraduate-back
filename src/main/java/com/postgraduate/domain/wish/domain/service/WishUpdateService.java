package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.wish.domain.entity.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishUpdateService {
    public void updateWishStatus(Wish wish) {
        wish.updateStatus();
    }
}

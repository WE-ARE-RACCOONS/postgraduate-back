package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.wish.domain.entity.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.wish.domain.entity.constant.Status.MATCHED;

@Service
@RequiredArgsConstructor
public class WishUpdateService {
    public void updateWishDone(Wish wish) {
        wish.updateStatus(MATCHED);
    }
}

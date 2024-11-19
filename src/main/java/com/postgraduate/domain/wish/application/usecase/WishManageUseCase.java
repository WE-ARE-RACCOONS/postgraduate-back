package com.postgraduate.domain.wish.application.usecase;

import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.wish.application.dto.request.WishCreateRequest;
import com.postgraduate.domain.wish.application.mapper.WishMapper;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishSaveService;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class WishManageUseCase {
    private final WishSaveService wishSaveService;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;

    public void applyWish(WishCreateRequest request, User user) {
        Wish wish = WishMapper.mapToWish(request, user);
        wishSaveService.saveWish(wish);
        bizppurioJuniorMessage.matchingWaiting(wish.getPhoneNumber());
    }
}

package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.MatchingWishResponses;
import com.postgraduate.admin.application.dto.res.WaitingWishResponses;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminWishService;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingSuccessRequest;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class AdminWishUseCase {
    private final AdminWishService adminWishService;
    private final AdminMapper adminMapper;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;

    @Transactional(readOnly = true)
    public WaitingWishResponses waitingWish() {
        return new WaitingWishResponses(adminWishService.findAllWaiting()
                .stream()
                .map(adminMapper::mapToWaitingWish)
                .toList());
    }

    @Transactional(readOnly = true)
    public MatchingWishResponses matchingWish() {
        return new MatchingWishResponses(adminWishService.findAllMatching()
                .stream()
                .map(adminMapper::mapToMatchedWish)
                .toList());
    }

    public void matchFin(Long wishId) {
        Wish wish = adminWishService.matchFin(wishId);
        bizppurioJuniorMessage.matchingSuccess(new JuniorMatchingSuccessRequest(
                        wish.getPhoneNumber(),
                        "고객님",
                        wish.getPostgradu(),
                        wish.getLab())
        ); //todo : 신청서 폼에서 postgradu, lab은 필수로 받아야 함 혹은 알림톡 형식을 변경해야 함
    }
}

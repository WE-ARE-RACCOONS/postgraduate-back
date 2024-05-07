package com.postgraduate.global.batch.cancel;

import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.global.bizppurio.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelWriter implements ItemWriter<CancelMentoring> {
    private final UserGetService userGetService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final SlackErrorMessage slackErrorMessage;
    private final CancelMentoringRepository cancelMentoringRepository;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;
    @Override
    public void write(Chunk<? extends CancelMentoring> chunk) {
        log.info("ChunkSize : {}", chunk.size());
        chunk.forEach(this::updateCancelWithAuto);
    }

    public void updateCancelWithAuto(CancelMentoring mentoring) {
        try {
            User user = userGetService.byUserId(mentoring.userId());
            paymentManageUseCase.refundPayByUser(mentoring.userId(), mentoring.paymentId());
            cancelMentoringRepository.updateAll(List.of(mentoring));
            cancelMentoringRepository.insertAll(List.of(mentoring));
            log.info("mentoringId : {} 자동 취소", mentoring.mentoringId());
            bizppurioJuniorMessage.mentoringRefuse(user);
        } catch (Exception ex) {
            log.error("mentoringId : {} 자동 취소 실패", mentoring.mentoringId());
            log.error(ex.getMessage());
            slackErrorMessage.sendSlackError(mentoring.mentoringId(), ex);
            throw ex;
        }
    }
}

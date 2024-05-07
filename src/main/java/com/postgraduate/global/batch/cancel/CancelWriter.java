package com.postgraduate.global.batch.cancel;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.global.bizppurio.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.postgraduate.domain.refuse.application.mapper.RefuseMapper.mapToRefuse;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelWriter implements ItemWriter<Mentoring> {
    private final MentoringGetService mentoringGetService;
    private final MentoringUpdateService mentoringUpdateService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final RefuseSaveService refuseSaveService;
    private final SlackErrorMessage slackErrorMessage;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;
    @Override
    public void write(Chunk<? extends Mentoring> chunk) {
        log.info("ChunkSize : {}", chunk.size());
        chunk.forEach(this::updateCancelWithAuto);
    }

    public void updateCancelWithAuto(Mentoring mentoring) {
        try {
//            paymentManageUseCase.refundPayByUser(mentoring.getUser(), mentoring.getPayment().getOrderId());
            Mentoring cancelMentoring = mentoringGetService.byMentoringIdWithLazy(mentoring.getMentoringId());
            mentoringUpdateService.updateCancel(cancelMentoring);
            Refuse refuse = mapToRefuse(cancelMentoring);
            refuseSaveService.save(refuse);
            log.info("mentoringId : {} 자동 취소", cancelMentoring.getMentoringId());
            bizppurioJuniorMessage.mentoringRefuse(cancelMentoring.getUser());

            if (cancelMentoring.getSenior().getSeniorId() == 12604)
                throw new IllegalArgumentException();

        } catch (Exception ex) {
            log.error("mentoringId : {} 자동 취소 실패", mentoring.getMentoringId());
            log.error(ex.getMessage());
//            slackErrorMessage.sendSlackError(mentoring, ex);
            throw ex;
        }
    }
}

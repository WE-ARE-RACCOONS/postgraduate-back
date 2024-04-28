package com.postgraduate.global.bizppurio.usecase;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class BizppurioSeniorMessage {
    private final BizppurioMapper mapper;
    private final BizppurioSend bizppurioSend;

    public void signUp(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToSeniorSignUpMessage(user));
    }

    public void mentoringApply(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToSeniorApplyMessage(user));
    }

    public void mentoringAccept(Senior senior, String time) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> {
            User user = senior.getUser();
            String chatLink = senior.getProfile().getChatLink();
            return mapper.mapToSeniorAcceptMessage(user, chatLink, time);
        });
    }

    public void certificationApprove(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToCertificationApprove(user));
    }

    public void certificationDenied(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToCertificationDenied(user));
    }

    public void mentoringFinish(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToSeniorFinish(user));
    }
}

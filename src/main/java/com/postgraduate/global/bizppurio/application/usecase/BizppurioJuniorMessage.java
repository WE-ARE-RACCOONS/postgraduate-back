package com.postgraduate.global.bizppurio.application.usecase;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingFailRequest;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingSuccessRequest;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingWaitingRequest;
import com.postgraduate.global.bizppurio.application.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class BizppurioJuniorMessage {
    private final BizppurioMapper mapper;
    private final BizppurioSend bizppurioSend;

    public void mentoringApply(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToJuniorApplyMessage(user));
    }

    public void mentoringAccept(User user, Senior senior, String time) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> {
            String chatLink = senior.getInfo().getChatLink();
            return mapper.mapToJuniorAcceptMessage(user, chatLink, time);
        });
    }

    public void mentoringRefuse(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToJuniorRefuseMessage(user));
    }

    public void mentoringFinish(User user) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToJuniorFinish(user));
    }

    public void matchingWaiting(JuniorMatchingWaitingRequest request) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToJuniorMatchingWaiting(request));
    }

    public void matchingFail(JuniorMatchingFailRequest request) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToJuniorMatchingFail(request));
    }

    public void matchingSuccess(JuniorMatchingSuccessRequest request) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> mapper.mapToJuniorMatchingSuccess(request));
    }
}

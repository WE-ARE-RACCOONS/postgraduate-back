package com.postgraduate.global.bizppurio.mapper;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.dto.req.CommonRequest;
import com.postgraduate.global.bizppurio.dto.req.ContentRequest;
import com.postgraduate.global.bizppurio.dto.req.content.Message;
import com.postgraduate.global.bizppurio.dto.req.content.SeniorApplyMessage;
import com.postgraduate.global.bizppurio.dto.req.content.SeniorSingUpMessage;
import com.postgraduate.global.bizppurio.dto.req.content.button.WebLinkButton;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class BizppurioMapper {
    @Value("${bizppurio.sender_key}")
    private static String senderKey;
    @Value("${bizppurio.id}")
    private static String id;
    @Value("${bizppurio.number}")
    private static String number;
    @Value("${bizppurio.template.senior_fin}")
    private static String seniorFin;
    @Value("${bizppurio.template.junior_fin}")
    private static String juniorFin;
    @Value("${bizppurio.template.certification_approve}")
    private static String certificationApprove;
    @Value("${bizppurio.template.certification_denied}")
    private static String certificationDenied;
    @Value("${bizppurio.template.mentoring_refuse}")
    private static String mentoringRefuse;
    @Value("${bizppurio.template.senior_mentoring_approve}")
    private static String seniorMentoringApprove;
    @Value("${bizppurio.template.junior_mentoring_approve}")
    private static String juniorMentoringApprove;
    @Value("${bizppurio.template.senior_mentoring_apply}")
    private static String seniorMentoringApply;
    @Value("${bizppurio.template.junior_mentoring_apply}")
    private static String juniorMentoringApply;
    @Value("${bizppurio.template.senior_signUp}")
    private static String seniorSignUp;

    private BizppurioMapper() {
        throw new IllegalArgumentException();
    }

    public static CommonRequest mapToSeniorSignUpMessage(User user) {
        String message = (
                user.getNickName() + " 선배님! 대학원 김선배에 회원가입을 해주셔서 정말 감사드립니다!"
                        + "\n\n"
                        + user.getNickName() + " 선배님 덕분에, 대학생 후배들의 랩실 입시가 더 쉬워질 수 있겠네요!"
                        + "\n\n"
                        + "매칭을 위해서 대학원 인증과 프로필 작성을 꼭 완료해주세요! 대학원 인증 뱃지가 없고 프로필이 비어있는 회원님들은 매칭에 어려움이 있을 수 있어요 (눈물)"
                        + "\n\n"
                        + "선배님에 성공적인 멘토링 매칭을 기원하겠습니다!(미소)"
        );

        WebLinkButton certification = new WebLinkButton("대학원 인증하기", "WL", "https://www.kimseonbae.com/signup/select/common-info/auth");
        WebLinkButton profile = new WebLinkButton("프로필 작성하기", "WL", "https://www.kimseonbae.com/signup/select/common-info/auth");
        WebLinkButton[] buttons = {certification, profile};
        Message messageBody = new SeniorSingUpMessage(message, senderKey, seniorSignUp, buttons);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToSeniorApplyMessage(User user) {
        String message = (
                user.getNickName() + " 선배님! 멘토링 신청이 들어왔어요!"
                        + "\n\n"
                        + "후배님으로부터 멘토링 신청이 들어왔습니다! 다음날 오후 11시 59분까지 신청해주시지 않으면 멘토링 신청이 자동 취소되니! 지금 수락하러 가볼까요? (미소)"
        );

        WebLinkButton mentoringCheck = new WebLinkButton("멘토링 신청 확인하기", "WL", "https://www.kimseonbae.com/senior/mentoring");
        WebLinkButton[] buttons = {mentoringCheck};
        Message messageBody = new SeniorApplyMessage(message, senderKey, seniorMentoringApply, buttons);
        return createCommonRequest(messageBody, user);
    }

    private static CommonRequest createCommonRequest(Message messageBody, User user) {
        String refKey = UUID.randomUUID().toString();
        ContentRequest contentRequest = new ContentRequest(messageBody);
        return new CommonRequest(id, "at", number , user.getPhoneNumber(), contentRequest, refKey);
    }
}
package com.postgraduate.global.bizppurio.mapper;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.dto.req.CommonRequest;
import com.postgraduate.global.bizppurio.dto.req.ContentRequest;
import com.postgraduate.global.bizppurio.dto.req.content.*;
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
    @Value("${bizppurio.template.senior_mentoring_fin}")
    private static String seniorMentoringFinish;
    @Value("${bizppurio.template.jnior_mentoring_fin}")
    private static String juniorMentoringFinish;
    @Value("${bizppurio.template.certification_approve}")
    private static String certificationApprove;
    @Value("${bizppurio.template.certification_denied}")
    private static String certificationDenied;
    @Value("${bizppurio.template.mentoring_refuse}")
    private static String mentoringRefuse;
    @Value("${bizppurio.template.senior_mentoring_accept}")
    private static String seniorMentoringAccept;
    @Value("${bizppurio.template.junior_mentoring_accept}")
    private static String juniorMentoringAccept;
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
        WebLinkButton profile = new WebLinkButton("프로필 작성하기", "WL", "https://www.kimseonbae.com/senior/edit-profile");
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

    public static CommonRequest mapToSeniorAcceptMessage(User user, String link, String time) {
        String message = (
                user.getNickName() + " 선배님! 후배와의 멘토링이 성사되었습니다. (축하)"
                        + "\n\n"
                        + time + " 에 진행되는 멘토링을 위해 아래 카카오톡 오픈 채팅방으로 참여해주세요! (해)"
                        + "\n"
                        + link
                        + "\n\n"
                        + "멘토링 진행 일시에 선배님께서 줌 또는 구글미트를 활용하여 비대면 대화 링크를 열어주시면 됩니다!"
        );
        Message messageBody = new SeniorAcceptMessage(message, senderKey, seniorMentoringAccept);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToCertificationApprove(User user) {
        String message = (
                user.getNickName() + " 선배님! 대학원 인증 신청이 승인되었어요!"
                        + "\n\n"
                        + "이제 프로필 작성만 완료되면, 선배님의 멘토링 신청률이 up!up! (축하)"
        );
        WebLinkButton profile = new WebLinkButton("프로필 작성하기", "WL", "https://www.kimseonbae.com/senior/edit-profile");
        WebLinkButton[] buttons = {profile};
        CertificationApproveMessage messageBody = new CertificationApproveMessage(message, senderKey, certificationApprove, buttons);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToCertificationDenied(User user) {
        String message = (
                user.getNickName() + " 선배님! 대학원 인증 신청이 반려되었어요. (눈물)"
                        + "\n\n"
                        + "첨부된 사진이 ‘대학원 학생증, 대학원 합격증, 연구실 멤버 확인 캡쳐본’ 중 하나가 맞는지 확인해주세요! 또한 첨부 서류가 충분히 식별 가능한 상태인지도 확인 부탁드려요! (최고)"
                        + "\n\n"
                        + "재신청 해주시면 빠르게 재심사 하도록 하겠습니다!"
        );
        WebLinkButton certification = new WebLinkButton("대학원 재인증하기", "WL", "https://www.kimseonbae.com/signup/select/common-info/auth");
        WebLinkButton[] buttons = {certification};
        CertificationDeniedMessage messageBody = new CertificationDeniedMessage(message, senderKey, certificationDenied, buttons);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToSeniorMentoringFin(User user) {
        String message = (
                user.getNickName() + " 선배님! 후배님과의 멘토링은 어떠셨나요?"
                        + "\n\n"
                        + "선배님도 만족하셨길 바라요! (미소)"
                        + "\n\n"
                        + "멘토링이 잘 진행되었다면, 오픈채팅방을 통해 후배님이 ‘대학원 김선배 - 내멘토링’에서 ‘멘토링 완료 확정하기’ 버튼을 눌러 주시길 유도 부탁드려요! (최고)"
                        + "\n\n"
                        + "멘토링 확정은 멘토링이 정상적으로 진행되었는지 확인하기 위함이며, 멘토링 완료 확정이 진행되지 않을시 정산이 지연될 수 있는점 양해 부탁드려요! (미소)"
                );
        SeniorFinishMessage messageBody = new SeniorFinishMessage(message, senderKey, seniorMentoringFinish);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToJuniorApplyMessage(User user) {
        String message = (
                user.getNickName() + " 님! 멘토링 결제 및 신청이 완료되었습니다!"
                        + "\n\n"
                        + user.getNickName() + " 님이 기다리시지 않게, 바로 선배님께 신청서를 전달할게요!"
                        + "\n\n"
                        + "선배님이 다음날 오후 11시 59분까지 멘토링을 수락해주시지 않으면 멘토링이 자동 취소 및 환불되니 유의해주세요! (미소)"
        );
        JuniorApplyMessage messageBody = new JuniorApplyMessage(message, senderKey, juniorMentoringApply);
        return createCommonRequest(messageBody, user);
    }

    private static CommonRequest createCommonRequest(Message messageBody, User user) {
        String refKey = UUID.randomUUID().toString();
        ContentRequest contentRequest = new ContentRequest(messageBody);
        return new CommonRequest(id, "at", number , user.getPhoneNumber(), contentRequest, refKey);
    }
}

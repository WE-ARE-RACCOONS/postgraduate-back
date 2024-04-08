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
    @Value("${bizppurio.template.junior_mentoring_refuse}")
    private static String juniorMentoringRefuse;
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
    @Value("${bizppurio.url.certification}")
    private static String certificationPage;
    @Value("${bizppurio.url.profile}")
    private static String profilePage;
    @Value("${bizppurio.url.senior_mentoring}")
    private static String seniorMentoringPage;
    @Value("${bizppurio.url.junior_mentoring}")
    private static String juniorMentoringPage;
    @Value("${bizppurio.url.main}")
    private static String mainPage;


    private static String type = "WL";

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

        WebLinkButton certification = new WebLinkButton("대학원 인증하기", type, certificationPage);
        WebLinkButton profile = new WebLinkButton("프로필 작성하기", "WL", profilePage);
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

        WebLinkButton mentoringCheck = new WebLinkButton("멘토링 신청 확인하기", type, seniorMentoringPage);
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
        WebLinkButton profile = new WebLinkButton("프로필 작성하기", type, profilePage);
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
        WebLinkButton certification = new WebLinkButton("대학원 재인증하기", type, certificationPage);
        WebLinkButton[] buttons = {certification};
        CertificationDeniedMessage messageBody = new CertificationDeniedMessage(message, senderKey, certificationDenied, buttons);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToSeniorFinish(User user) {
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

    public static CommonRequest mapToJuniorAcceptMessage(User user, String link, String time) {
        String message = (
                user.getNickName() + " 님! 선배님이 멘토링을 수락하셨어요!"
                        + "\n\n"
                        + time + " 일정으로 멘토링이 수락되었답니다! 멘토링을 통해 많은 걸 얻어 가실 수 있길 바랄게요!"
                        + "\n\n"
                        + "멘토링 시간이 되면, 아래 오픈채팅방으로 접속 후, 멘토링 시간에 선배님이 생성하신 비대면 링크로 접속해 진행하시면 됩니다. (하트)"
                        + "\n"
                        + link
                        + "\n\n"
                        + "멘토링 시간은 꼭 지켜주세요! (해)"
        );
        JuniorAcceptMessage messageBody = new JuniorAcceptMessage(message, senderKey, juniorMentoringAccept);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToJuniorRefuseMessage(User user) {
        String message = (
                user.getNickName() + " 님! 신청하신 멘토링이 선배님의 개인사정으로 인해 진행이 어렵게 되었어요. (눈물)"
                        + "\n\n" +
                        "신청하신 멘토링은 취소 및 환불 처리가 진행되며 카드사별 영업일 기준 1-3일 소요됩니다!"
                        + "\n\n" +
                        "혹시 멘토링을 진행하고 싶었던 다른 선배님이 있다면 지금 신청해보세요!"
        );
        WebLinkButton otherSenior = new WebLinkButton("다른 선배 보러기가", type, mainPage);
        WebLinkButton[] buttons = {otherSenior};
        JuniorRefuseMessage messageBody = new JuniorRefuseMessage(message, senderKey, juniorMentoringRefuse, buttons);
        return createCommonRequest(messageBody, user);
    }

    public static CommonRequest mapToJuniorFinish(User user) {
        String message = (
                user.getNickName() + " 님! 멘토링은 만족스러우셨을까요?"
                        + "\n\n" +
                        "멘토링을 잘 진행하셨다면,"
                        + "\n"
                        +"실제 멘토링이 진행되었는지 확인을 위해 아래 버튼을 누른 후 웹사이트에서 ‘멘토링 완료 확정하기’를 눌러주세요! (음표)"
                        + "\n\n"
                        + "혹시 멘토링 시간이 달라졌거나, 멘토링을 진행하지 못한 경우, 고객센터를 통해 꼭 알려주세요! (콜)"
                        + "\n\n"
                        + "별도의 문의가 없다면, 3일 후 멘토링이 자동 확정되며 선배님께 보수비가 정산됩니다!"
        );
        WebLinkButton mentoringFinish = new WebLinkButton("진행 확정하러가기", type, juniorMentoringPage);
        WebLinkButton[] buttons = {mentoringFinish};

        JuniorFinishMessage messageBody = new JuniorFinishMessage(message, senderKey, juniorMentoringFinish, buttons);
        return createCommonRequest(messageBody, user);
    }

    private static CommonRequest createCommonRequest(Message messageBody, User user) {
        String refKey = UUID.randomUUID().toString();
        ContentRequest contentRequest = new ContentRequest(messageBody);
        return new CommonRequest(id, "at", number , user.getPhoneNumber(), contentRequest, refKey);
    }
}

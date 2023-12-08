package com.postgraduate.domain.admin.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.admin.application.dto.*;
import com.postgraduate.domain.admin.application.dto.res.*;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;

import java.time.LocalDateTime;

public class AdminMapper {

    public static CertificationDetailsResponse mapToCertificationInfo(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new CertificationDetailsResponse(
                senior.getCertification(),
                user.getNickName(),
                user.getPhoneNumber(),
                info.getPostgradu(),
                info.getMajor(),
                info.getField(),
                info.getLab(),
                info.getProfessor(),
                info.getKeyword()
        );
    }

    public static CertificationProfile mapToCertificationProfile(Senior senior) {
        return CertificationProfile.builder()
                .time(senior.getProfile().getTime())
                .term(senior.getProfile().getTerm())
                .info(senior.getProfile().getInfo())
                .build();
    }

    public static CertificationResponse mapToCertification(Senior senior) {
        return CertificationResponse.builder()
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .createdAt(senior.getCreatedAt())
                .build();
    }

    public static UserInfo mapToUserInfo(Wish wish) {
        User user = wish.getUser();
        return new UserInfo(
                user.getUserId(),
                user.getNickName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getMarketingReceive(),
                wish.getMatchingReceive(),
                wish.getWishId(),
                user.getRole()
        );
    }

//
//    public static UserWithSeniorResponse mapToUserWithSeniorResponse(User user, Long seniorId) {
//        return UserWithSeniorResponse.builder()
//                .userId(user.getUserId())
//                .nickName(user.getNickName())
//                .marketingReceive(user.getMarketingReceive())
//                .matchingReceive(user.getHope().getMatchingReceive())
//                .createdAt(user.getCreatedAt())
//                .seniorId(seniorId)
//                .build();
//    }

    public static SeniorInfo mapToSeniorInfo(Senior senior, SalaryStatus salaryStatus, Boolean isUser) {
        User user = senior.getUser();
        return new SeniorInfo(
                senior.getSeniorId(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getStatus(),
                salaryStatus,
                user.getMarketingReceive(),
                isUser
        );
    }

    public static MentoringInfo mapToMentoringInfo(Mentoring mentoring) {
         User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new MentoringInfo(
                mentoring.getMentoringId(),
                mentoring.getStatus(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getUser().getNickName(),
                senior.getUser().getPhoneNumber()
        );
    }

    public static PaymentInfo mapToPaymentInfo(Payment payment) {
        Mentoring mentoring = payment.getMentoring();
        return new PaymentInfo(
                payment.getPaymentId(),
                mentoring.getMentoringId(),
                mentoring.getUser().getNickName(),
                mentoring.getSenior().getUser().getNickName(),
                payment.getCreatedAt(),
                mentoring.getPay()
        );
    }

    public static SalaryInfo mapToSalaryResponse(Senior senior, Account account, String accountNumber, int totalAmount, LocalDateTime salaryDoneDate) {
        User user = senior.getUser();
        return new SalaryInfo(
                user.getNickName(),
                user.getPhoneNumber(),
                totalAmount,
                account.getAccountHolder(),
                account.getBank(),
                accountNumber,
                salaryDoneDate
        );
    }

    public static SalaryInfo mapToSalaryResponse(Senior senior, int totalAmount, LocalDateTime salaryDoneDate) {
        User user = senior.getUser();
        return new SalaryInfo(
                user.getNickName(),
                user.getPhoneNumber(),
                totalAmount,
                null,
                null,
                null,
                salaryDoneDate
        );
    }

    public static SalaryDetailsResponse mapToSalaryDetailsResponse(Senior senior, Account account, String accountNumber, int totalAmount, SalaryStatus status) {
        User user = senior.getUser();
        return new SalaryDetailsResponse(
                user.getNickName(),
                user.getPhoneNumber(),
                totalAmount,
                account.getAccountHolder(),
                account.getBank(),
                accountNumber,
                status
        );
    }

    public static SalaryDetailsResponse mapToSalaryDetailsResponse(Senior senior, int totalAmount, SalaryStatus status) {
        User user = senior.getUser();
        return new SalaryDetailsResponse(
                user.getNickName(),
                user.getPhoneNumber(),
                totalAmount,
                null,
                null,
                null,
                status
        );
    }

    public static MentoringWithPaymentResponse mapToMentoringWithPaymentResponse(Payment payment, Mentoring mentoring) {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new MentoringWithPaymentResponse(
                payment.getPaymentId(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getUser().getNickName(),
                senior.getUser().getPhoneNumber(),
                mentoring.getDate(),
                senior.getProfile().getTerm(),
                (int) (mentoring.getPay() * 1.2),
                (int) (mentoring.getPay() * 0.2)
        );
    }
}

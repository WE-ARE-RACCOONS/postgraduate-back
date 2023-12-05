package com.postgraduate.domain.admin.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.admin.application.dto.CertificationProfile;
import com.postgraduate.domain.admin.application.dto.res.*;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public static UserResponse mapToUserResponse(User user, Optional<Wish> wish) {
        Long wishId = wish.map(Wish::getWishId).orElse(null);
        Boolean matchingReceive = wish.map(Wish::getMatchingReceive).orElse(null);
        return UserResponse.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .marketingReceive(user.getMarketingReceive())
                .matchingReceive(matchingReceive)
                .wishId(wishId)
                .role(user.getRole())
                .build();
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

    public static SeniorResponse mapToSeniorResponse(Senior senior, SalaryStatus salaryStatus, Boolean isUser) {
        User user = senior.getUser();
        return SeniorResponse.builder()
                .seniorId(senior.getSeniorId())
                .nickName(user.getNickName())
                .phoneNumber(user.getPhoneNumber())
                .status(senior.getStatus())
                .salaryStatus(salaryStatus)
                .marketingReceive(user.getMarketingReceive())
                .isUser(isUser)
                .build();
    }

    public static MentoringResponse mapToMentoringResponse(Mentoring mentoring) {
         User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return MentoringResponse.builder()
                .mentoringId(mentoring.getMentoringId())
                .status(mentoring.getStatus())
                .userNickName(user.getNickName())
                .userPhoneNumber(user.getPhoneNumber())
                .seniorNickName(senior.getUser().getNickName())
                .seniorPhoneNumber(senior.getUser().getPhoneNumber())
                .build();
    }

    public static PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .mentoringId(payment.getMentoring().getMentoringId())
                .userNickName(payment.getMentoring().getUser().getNickName())
                .seniorNickName(payment.getMentoring().getSenior().getUser().getNickName())
                .createdAt(payment.getCreatedAt())
                .pay(payment.getMentoring().getPay())
                .build();
    }

    public static SalariesResponse mapToSalaryResponse(Senior senior, Account account, String accountNumber, int totalAmount, LocalDateTime salaryDoneDate) {
        User user = senior.getUser();
        return new SalariesResponse(
                user.getNickName(),
                user.getPhoneNumber(),
                totalAmount,
                account.getAccountHolder(),
                account.getBank(),
                accountNumber,
                salaryDoneDate
        );
    }

    public static SalaryDetailsResponse mapToSalaryResponse(Senior senior, Account account, String accountNumber, int totalAmount, SalaryStatus status) {
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

    public static MentoringWithPaymentResponse mapToMentoringWithPaymentResponse(Mentoring mentoring) {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new MentoringWithPaymentResponse(
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

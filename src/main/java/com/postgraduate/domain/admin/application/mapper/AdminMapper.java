package com.postgraduate.domain.admin.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.admin.application.dto.*;
import com.postgraduate.domain.admin.application.dto.res.*;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.wish.application.mapper.dto.res.WishResponse;
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
                senior.getCreatedAt(),
                info.getPostgradu(),
                info.getMajor(),
                info.getField(),
                info.getLab(),
                info.getProfessor(),
                info.getKeyword()
        );
    }

    public static UserInfo mapToUserInfo(Wish wish) {
        User user = wish.getUser();
        Boolean isSenior = user.getRole() == Role.SENIOR;
        Long wishId = wish.getWishId();
        if (wish.getMajor() == null && wish.getField() == null) {
            wishId = null;
        }
        return new UserInfo(
                user.getUserId(),
                user.getNickName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getMarketingReceive(),
                wish.getMatchingReceive(),
                wishId,
                wish.getStatus(),
                isSenior
        );
    }

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
        return new MentoringInfo(
                mentoring.getMentoringId(),
                mentoring.getStatus(),
                user.getNickName(),
                user.getPhoneNumber(),
                mentoring.getCreatedAt()
        );
    }

    public static UserMentoringInfo mapToUserMentoringInfo(User user) {
        return new UserMentoringInfo(
                user.getNickName(),
                user.getPhoneNumber()
        );
    }

    public static UserMentoringInfo mapToUserMentoringInfo(Senior senior) {
        User user = senior.getUser();
        return new UserMentoringInfo(
                user.getNickName(),
                user.getPhoneNumber()
        );
    }

    public static PaymentInfo mapToPaymentInfo(Payment payment) {
        Mentoring mentoring = payment.getMentoring();
        User user = mentoring.getUser();
        return new PaymentInfo(
                payment.getPaymentId(),
                mentoring.getMentoringId(),
                user.getNickName(),
                user.getPhoneNumber(),
                payment.getCreatedAt(),
                mentoring.getPay(),
                payment.getStatus()
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
                mentoring.getTerm(),
                (int) (mentoring.getPay() * 1.2),
                (int) (mentoring.getPay() * 0.2)
        );
    }

    public static WishResponse mapToWishResponse(Wish wish) {
        User user = wish.getUser();
        return new WishResponse(
                user.getNickName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getMarketingReceive(),
                wish.getMatchingReceive(),
                wish.getMajor(),
                wish.getField());
    }
}

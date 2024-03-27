package com.postgraduate.domain.adminssr.application.mapper;

import com.postgraduate.domain.adminssr.application.dto.res.*;
import com.postgraduate.domain.adminssr.application.dto.res.SeniorInfo;
import com.postgraduate.domain.adminssr.application.dto.res.UnSettledSalaryInfo;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.wish.domain.entity.Wish;

public class AdminSsrMapper {
    private AdminSsrMapper() {
        throw new IllegalStateException();
    }

    public static CertificationDetailsResponse mapToCertificationInfo(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new CertificationDetailsResponse(
                senior.getSeniorId(),
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
        return new UserInfo(
                user.getUserId(),
                user.getNickName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getMarketingReceive(),
                wish.getMatchingReceive(),
                wish.getWishId(),
                wish.getStatus(),
                isSenior
        );
    }

    public static SeniorInfo mapToSeniorInfo(Senior senior, int totalAmount, Boolean isUser) {
        User user = senior.getUser();
        return new SeniorInfo(
                senior.getSeniorId(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getStatus(),
                totalAmount,
                user.getMarketingReceive(),
                isUser
        );
    }

    public static MentoringInfo mapToMentoringInfoWithSenior(Mentoring mentoring) {
        User user = mentoring.getUser();
        return new MentoringInfo(
                mentoring.getMentoringId(),
                mentoring.getStatus(),
                user.getNickName(),
                user.getPhoneNumber(),
                mentoring.getCreatedAt(),
                mentoring.getDate()
        );
    }

    public static MentoringInfo mapToMentoringInfoWithUser(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        User user = senior.getUser();
        return new MentoringInfo(
                mentoring.getMentoringId(),
                mentoring.getStatus(),
                user.getNickName(),
                user.getPhoneNumber(),
                mentoring.getCreatedAt(),
                mentoring.getDate()
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

    public static PaymentInfo mapToPaymentInfo(Mentoring mentoring) {
        Payment payment = mentoring.getPayment();
        User user = mentoring.getUser();
        return new PaymentInfo(
                payment.getPaymentId(),
                mentoring.getMentoringId(),
                user.getNickName(),
                user.getPhoneNumber(),
                payment.getPaidAt(),
                payment.getPay(),
                payment.getStatus()
        );
    }

    public static PaymentInfo mapToPaymentInfo(Payment payment, Mentoring mentoring) {
        User user = payment.getUser();
        return new PaymentInfo(
                payment.getPaymentId(),
                mentoring.getMentoringId(),
                user.getNickName(),
                user.getPhoneNumber(),
                payment.getPaidAt(),
                payment.getPay(),
                payment.getStatus()
        );
    }

    public static PaymentInfo mapToPaymentInfo(Payment payment) {
        User user = payment.getUser();
        return new PaymentInfo(
                payment.getPaymentId(),
                null,
                user.getNickName(),
                user.getPhoneNumber(),
                payment.getPaidAt(),
                payment.getPay(),
                payment.getStatus()
        );
    }

    public static SalaryInfoWithOutId mapToSalaryResponse(Senior senior, String accountNumber, Salary salary) {
        User user = senior.getUser();
        SalaryAccount account = salary.getAccount();
        return new SalaryInfoWithOutId(
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                account.getAccountHolder(),
                account.getBank(),
                accountNumber,
                salary.getSalaryDoneDate()
        );
    }

    public static SalaryInfoWithOutId mapToSalaryResponse(Senior senior, Salary salary) {
        User user = senior.getUser();
        return new SalaryInfoWithOutId (
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                salary.getSalaryDoneDate()
        );
    }

    public static UnSettledSalaryInfo mapToUnSettledSalaryResponse(Salary salary) {
        Senior senior = salary.getSenior();
        User user = senior.getUser();
        return new UnSettledSalaryInfo(
                salary.getSalaryId(),
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                salary.getSalaryDate()
        );
    }

    public static UnSettledSalaryInfo mapToUnSettledSalaryResponse(Salary salary, String accountNumber) {
        Senior senior = salary.getSenior();
        User user = senior.getUser();
        SalaryAccount account = salary.getAccount();
        return new UnSettledSalaryInfo(
                salary.getSalaryId(),
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                account.getAccountHolder(),
                account.getBank(),
                accountNumber,
                salary.getSalaryDate()
        );
    }

    public static MentoringWithPaymentResponse mapToMentoringWithPaymentResponse(Mentoring mentoring) {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        Payment payment = mentoring.getPayment();
        return new MentoringWithPaymentResponse(
                mentoring.getMentoringId(),
                payment.getPaymentId(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getUser().getNickName(),
                senior.getUser().getPhoneNumber(),
                mentoring.getDate(),
                mentoring.getTerm(),
                payment.getPay(),
                4000
        );
    }

    public static WishResponse mapToWishResponse(Wish wish) {
        User user = wish.getUser();
        return new WishResponse(
                wish.getWishId(),
                user.getNickName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getMarketingReceive(),
                wish.getMatchingReceive(),
                wish.getMajor(),
                wish.getField());
    }
}

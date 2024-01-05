package com.postgraduate.domain.admin.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.admin.application.dto.*;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.wish.application.dto.res.WishResponse;
import com.postgraduate.domain.wish.domain.entity.Wish;

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

    public static MentoringInfo mapToSeniorMentoringInfo(Mentoring mentoring) {
        User user = mentoring.getSenior().getUser();
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
                payment.getPaidAt(),
                payment.getPay(),
                payment.getStatus()
        );
    }

    public static SalaryInfo mapToSalaryResponse(Senior senior, String accountNumber, Salary salary) {
        User user = senior.getUser();
        return new SalaryInfo(
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                salary.getAccountHolder(),
                salary.getBank(),
                accountNumber,
                salary.getSalaryDoneDate()
        );
    }

    public static SalaryInfo mapToSalaryResponse(Senior senior, Salary salary) {
        User user = senior.getUser();
        return new SalaryInfo(
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                salary.getSalaryDoneDate()
        );
    }

    public static SalaryDetailsResponse mapToSalaryDetailsResponse(Senior senior, Account account, String accountNumber, int totalAmount, Boolean status) {
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

    public static SalaryDetailsResponse mapToSalaryDetailsResponse(Senior senior, int totalAmount, Boolean status) {
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
                payment.getPay(),
                (int) (payment.getPay() * 0.2)
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

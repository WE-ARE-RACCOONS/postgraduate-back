package com.postgraduate.admin.application.mapper;

import com.postgraduate.admin.application.dto.res.*;
import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.application.utils.UserUtils;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.postgraduate.domain.mentoring.domain.entity.constant.TermUnit.SHORT;

@RequiredArgsConstructor
@Component
@Slf4j
public class AdminMapper {
    private final UserUtils userUtils;

    public CertificationDetailsResponse mapToCertificationInfo(Senior senior) {
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

    public UserInfo mapToUserInfo(MemberRole memberRole) {
        User user = memberRole.getUser();
        return new UserInfo(
                user.getUserId(),
                user.getNickName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getMarketingReceive(),
                user.isSenior()
        );
    }


    public SeniorInfo mapToSeniorInfo(Salary salary) {
        Senior senior = salary.getSenior();
        User user = senior.getUser();
        return new SeniorInfo(
                senior.getSeniorId(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getStatus(),
                salary.getTotalAmount(),
                user.getMarketingReceive(),
                user.isJunior()
        );
    }

    public MentoringInfo mapToMentoringInfoWithUser(Mentoring mentoring) {
        if (mentoring.getSenior() == null || mentoring.getSenior().getUser().isDelete()) {
            return getMentoringInfo(mentoring, userUtils.getArchiveUser());
        }
        Senior senior = mentoring.getSenior();
        return getMentoringInfo(mentoring, senior.getUser());
    }

    public MentoringInfo mapToMentoringInfoWithSenior(Mentoring mentoring) {
        if (mentoring.getUser() == null || mentoring.getUser().isDelete()) {
            return getMentoringInfo(mentoring, userUtils.getArchiveUser());
        }
        User user = mentoring.getUser();
        return getMentoringInfo(mentoring, user);
    }

    @NotNull
    private MentoringInfo getMentoringInfo(Mentoring mentoring, User user) {
        return new MentoringInfo(
                mentoring.getMentoringId(),
                mentoring.getStatus(),
                user.getNickName(),
                user.getPhoneNumber(),
                mentoring.getCreatedAt(),
                mentoring.getDate()
        );
    }


    public UserInfoBasic mapToUserMentoringInfo(User user) {
        return new UserInfoBasic(
                user.getNickName(),
                user.getPhoneNumber()
        );
    }

    public UserInfoBasic mapToUserMentoringInfo(Senior senior) {
        User user = senior.getUser();
        return new UserInfoBasic(
                user.getNickName(),
                user.getPhoneNumber()
        );
    }

    public PaymentInfo mapToPaymentInfo(Payment payment, Mentoring mentoring) {
        if (payment.getUser() == null) {
            return getPaymentInfo(payment, mentoring.getMentoringId(), userUtils.getArchiveUser());
        }
        User user = payment.getUser();
        return getPaymentInfo(payment, mentoring.getMentoringId(), user);
    }

    @NotNull
    private PaymentInfo getPaymentInfo(Payment payment, Long mentoring, User user) {
        return new PaymentInfo(
                payment.getPaymentId(),
                mentoring,
                user.getNickName(),
                user.getPhoneNumber(),
                payment.getPaidAt(),
                payment.getPay(),
                payment.getStatus()
        );
    }

    public PaymentInfo mapToPaymentInfo(Payment payment) {
        User user = payment.getUser();
        return getPaymentInfo(payment, null, user);
    }

    public SalaryInfo mapToSalaryResponse(Senior senior, String accountNumber, Salary salary) {
        User user = senior.getUser();
        SalaryAccount account = salary.getAccount();
        return new SalaryInfo(
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                account.getAccountHolder(),
                account.getBank(),
                accountNumber,
                salary.getSalaryDoneDate()
        );
    }

    public SalaryInfo mapToSalaryResponse(Salary salary) {
        User user = userUtils.getArchiveUser();
        return new SalaryInfo(
                user.getNickName(),
                user.getPhoneNumber(),
                salary.getTotalAmount(),
                salary.getSalaryDoneDate()
        );
    }


    public UnSettledSalaryInfo mapToUnSettledSalaryResponse(Salary salary) {
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

    public UnSettledSalaryInfo mapToUnSettledSalaryResponse(Salary salary, String accountNumber) {
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

    public MentoringWithPaymentResponse mapToMentoringWithPaymentResponse(Mentoring mentoring) {
        User user;
        User senior;
        if (mentoring.getUser() == null || mentoring.getUser().isDelete()) {
            log.info("user is Null");
            user = userUtils.getArchiveUser();
        }
        else {
            user = mentoring.getUser();
        }
        if (mentoring.getSenior().getUser() == null || mentoring.getSenior().getUser().isDelete()) {
            log.info("senior is Null");
            senior = userUtils.getArchiveUser();
        }
        else {
            senior = mentoring.getSenior().getUser();
        }
        Payment payment = mentoring.getPayment();
        return getMentoringWithPaymentResponse(mentoring, user, senior, payment);
    }

    @NotNull
    private static MentoringWithPaymentResponse getMentoringWithPaymentResponse(Mentoring mentoring, User user, User senior, Payment payment) {
        return new MentoringWithPaymentResponse(
                mentoring.getMentoringId(),
                payment.getPaymentId(),
                user.getNickName(),
                user.getPhoneNumber(),
                senior.getNickName(),
                senior.getPhoneNumber(),
                mentoring.getDate(),
                mentoring.getTerm(),
                payment.getPay(),
                SHORT.getCharge()
        );
    }

    public WaitingWishResponse mapToWaitingWish(Wish wish) {
        return new WaitingWishResponse(
                wish.getWishId(),
                wish.getField(),
                wish.getPostgradu(),
                wish.getProfessor(),
                wish.getLab(),
                wish.getPhoneNumber(),
                wish.getCreatedAt()
        );
    }

    public MatchingWishResponse mapToMatchedWish(Wish wish) {
        return new MatchingWishResponse(
                wish.getWishId(),
                wish.getField(),
                wish.getPostgradu(),
                wish.getProfessor(),
                wish.getLab(),
                wish.getPhoneNumber(),
                wish.getCreatedAt(),
                wish.getUpdatedAt()
        );
    }
}

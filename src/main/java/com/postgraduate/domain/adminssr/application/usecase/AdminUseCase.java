package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.admin.application.dto.*;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringManageResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.adminssr.application.dto.req.Login;
import com.postgraduate.domain.adminssr.domain.AuthGetService;
import com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.exception.SalaryNotYetException;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.SeniorCertificationException;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.admin.application.dto.res.WishResponse;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.domain.wish.domain.service.WishUpdateService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.YET;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.CANCEL;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.getStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUseCase {
    private final UserGetService userGetService;
    private final AuthGetService authGetService;
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final WishGetService wishGetService;
    private final WishUpdateService wishUpdateService;
    private final PaymentGetService paymentGetService;
    private final MentoringGetService mentoringGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final EncryptorUtils encryptorUtils;
    private final SalaryUpdateService salaryUpdateService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final MentoringUpdateService mentoringUpdateService;

    public User login(Login loginForm) {
        return authGetService.login(loginForm.nickName(), loginForm.phoneNumber());
    }

    public List<SeniorInfo> allSenior() {
        List<Senior> seniors = seniorGetService.allSeniorId();
        return seniors.stream()
                .map(senior -> {
                    Salary salary = salaryGetService.bySenior(senior);
                    SalaryStatus salaryStatus = getStatus(salary);
                    Optional<Wish> wish = wishGetService.byUser(senior.getUser());
                    return AdminMapper.mapToSeniorInfo(senior, salaryStatus, wish.isPresent());
                })
                .toList();
    }

    public List<UserInfo> userInfos() {
        List<Wish> all = wishGetService.all();
        return all.stream()
                .map(AdminMapper::mapToUserInfo)
                .toList();
    }

    public List<SalaryInfo> salaryInfos() {
        List<Salary> all = salaryGetService.findAll();
        return all.stream()
                .map(salary -> {
                    if (salary.getAccountNumber() == null)
                        return AdminMapper.mapToSalaryResponse(salary.getSenior(), salary);
                    String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
                    return AdminMapper.mapToSalaryResponse(salary.getSenior(), accountNumber, salary);
                })
                .toList();
    }

    public List<PaymentInfo> paymentInfos() {
        List<Payment> all = paymentGetService.all();
        return all.stream()
                .map(payment -> {
                    Mentoring mentoring = mentoringGetService.byPayment(payment);
                    return AdminMapper.mapToPaymentInfo(payment, mentoring);
                })
                .toList();
    }

    public CertificationDetailsResponse getCertification(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        if (senior.getStatus() == Status.APPROVE)
            throw new SeniorCertificationException();
        return AdminMapper.mapToCertificationInfo(senior);
    }

    public void updateNotApprove(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.updateCertificationStatus(senior, Status.NOT_APPROVE);
    }

    public void updateApprove(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.updateCertificationStatus(senior, Status.APPROVE);
    }

    public MentoringManageResponse seniorMentorings(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        UserMentoringInfo seniorInfo = AdminMapper.mapToUserMentoringInfo(senior);
        return new MentoringManageResponse(mentoringInfos, seniorInfo);
    }

    public SalaryInfo seniorSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        SalaryStatus status = getStatus(salary);
        if (status != YET)
            throw new SalaryNotYetException();
        if (salary.getAccountNumber() == null)
            return AdminMapper.mapToSalaryResponse(senior, salary);
        String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
        return AdminMapper.mapToSalaryResponse(senior, accountNumber, salary);
    }

    public WishResponse wishInfo(Long userId) {
        Wish wish = wishGetService.byUserId(userId);
        return AdminMapper.mapToWishResponse(wish);
    }

    public MentoringManageResponse userMentoringInfos(Long userId) {
        User user = userGetService.byUserId(userId);
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        UserMentoringInfo userInfo = AdminMapper.mapToUserMentoringInfo(user);
        return new MentoringManageResponse(mentoringInfos, userInfo);
    }

    public MentoringWithPaymentResponse paymentMentoringInfo(Long paymentId) {
        Payment payment = paymentGetService.byId(paymentId);
        Mentoring mentoring = mentoringGetService.byPayment(payment);
        return AdminMapper.mapToMentoringWithPaymentResponse(mentoring);
    }

    public void wishDone(Long wishId) {
        Wish wish = wishGetService.byWishId(wishId);
        wishUpdateService.updateWishStatus(wish);
    }

    public void salaryDone(Long salaryId) {
        Salary salary = salaryGetService.bySalaryId(salaryId);
        salaryUpdateService.updateStatus(salary, true);
    }

    public void refundMentoring(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayByAdmin(user, payment.getPaymentId());
        if (mentoring.getStatus() == DONE) {
            Senior senior = mentoring.getSenior();
            Salary salary = salaryGetService.bySenior(senior);
            salaryUpdateService.minusTotalAmount(salary);
        }
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
    }
}

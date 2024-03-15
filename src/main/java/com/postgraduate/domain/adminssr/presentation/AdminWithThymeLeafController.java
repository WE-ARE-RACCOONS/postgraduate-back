package com.postgraduate.domain.adminssr.presentation;

import com.postgraduate.domain.admin.application.dto.*;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringManageResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.dto.res.WishResponse;
import com.postgraduate.domain.adminssr.application.dto.req.Login;
import com.postgraduate.domain.adminssr.application.usecase.AdminUseCase;
import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/adminServer")
public class AdminWithThymeLeafController {
    private final AdminUseCase adminUseCase;
    private final JwtUseCase jwtUseCase;

    @GetMapping("/loginForm")
    public String loginForm(Model model, Login loginForm) {
        model.addAttribute("loginForm", loginForm);
        return "adminLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Login loginForm, Model model) {
        User user = adminUseCase.login(loginForm);
        JwtTokenResponse jwtTokenResponse = jwtUseCase.signIn(user);
        model.addAttribute("tokenResponse", jwtTokenResponse);
        return "adminMain";
    }

    @GetMapping("/seniorInfo")
    public String seniorInfo(Model model) {
        List<SeniorInfo> seniorInfos = adminUseCase.allSenior();
        model.addAttribute("seniorInfos", seniorInfos);
        return "adminSenior";
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model) {
        List<UserInfo> userInfos = adminUseCase.userInfos();
        model.addAttribute("userInfos", userInfos);
        return "adminUser";
    }

    @GetMapping("/paymentInfo")
    public String paymentInfo(Model model) {
        List<PaymentInfo> paymentInfos = adminUseCase.paymentInfos();
        model.addAttribute("paymentInfos", paymentInfos);
        return "adminPayment";
    }

    @GetMapping("/salaryInfo")
    public String salaryInfo(Model model) {
        List<SalaryInfo> salaryInfos = adminUseCase.salaryInfos();
        model.addAttribute("salaryInfo", salaryInfos);
        return "adminSalary";
    }

    @GetMapping("/certification/{seniorId}")
    public String certification(@PathVariable Long seniorId, Model model) {
        CertificationDetailsResponse certification = adminUseCase.getCertification(seniorId);
        model.addAttribute("certificationInfo", certification);
        return "seniorCertification";
    }

    @PostMapping("/certification/{seniorId}/refuse")
    public String certificationRefuse(@PathVariable Long seniorId) {
        adminUseCase.updateNotApprove(seniorId);
        return "adminEmpty";
    }

    @PostMapping("/certification/{seniorId}/approve")
    public String certificationApprove(@PathVariable Long seniorId) {
        adminUseCase.updateApprove(seniorId);
        return "adminEmpty";
    }

    @GetMapping("/mentoring/{seniorId}")
    public String seniorMentoringInfo(@PathVariable Long seniorId, Model model) {
        MentoringManageResponse mentoringInfos = adminUseCase.seniorMentorings(seniorId);
        model.addAttribute("mentoringInfos", mentoringInfos);
        return "seniorMentoring";
    }

    @PostMapping("/mentoring/refund/{mentoringId}")
    public String refundMentoring(@AuthenticationPrincipal User user, @PathVariable Long mentoringId) {
        adminUseCase.refundMentoring(user, mentoringId);
        return "adminEmpty";
    }

    @GetMapping("/salary/{seniorId}")
    public String seniorSalaryInfo(@PathVariable Long seniorId, Model model) {
        SalaryInfo salaryInfo = adminUseCase.seniorSalary(seniorId);
        model.addAttribute("salaryInfo", salaryInfo);
        return "seniorSalary";
    }

    @PostMapping("/salary/done/{salaryId}")
    public String salaryDone(@PathVariable Long salaryId) {
        adminUseCase.salaryDone(salaryId);
        return "adminEmpty";
    }

    @GetMapping("/user/matching/{userId}")
    public String userMatching(@PathVariable Long userId, Model model) {
        WishResponse wishResponse = adminUseCase.wishInfo(userId);
        model.addAttribute("wishInfo", wishResponse);
        return "userWish";
    }

    @PostMapping("/wish/done/{wishId}")
    public String wishDone(@PathVariable Long wishId) {
        adminUseCase.wishDone(wishId);
        return "adminEmpty";
    }

    @GetMapping("/user/mentoring/{userId}")
    public String userMentoring(@PathVariable Long userId, Model model) {
        MentoringManageResponse mentoringInfos = adminUseCase.userMentoringInfos(userId);
        model.addAttribute("mentoringInfos", mentoringInfos);
        return "userMentoring";
    }

    @GetMapping("/payment/mentoring/{paymentId}")
    public String paymentMentoring(@PathVariable Long paymentId, Model model) {
        MentoringWithPaymentResponse mentoringInfo = adminUseCase.paymentMentoringInfo(paymentId);
        model.addAttribute("mentoringInfo", mentoringInfo);
        return "paymentMentoring";
    }
}

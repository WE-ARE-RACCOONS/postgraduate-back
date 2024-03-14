package com.postgraduate.domain.adminssr.presentation;

import com.postgraduate.domain.admin.application.dto.*;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.adminssr.application.dto.req.Login;
import com.postgraduate.domain.adminssr.application.usecase.AdminUseCase;
import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/juniorInfo")
    public String juniorInfo(Model model) {
        List<UserInfo> userInfos = adminUseCase.userInfos();
        model.addAttribute("userInfos", userInfos);
        return "adminJunior";
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
        List<MentoringInfo> mentoringInfos = adminUseCase.seniorMentorings(seniorId);
        model.addAttribute("mentoringInfos", mentoringInfos);
        return "seniorMentoring";
    }

    @PostMapping("/mentoring/refund/{mentoringId}")
    public String refundMentoring(@PathVariable Long mentoringId) {
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
        return "adminEmpty";
    }
}

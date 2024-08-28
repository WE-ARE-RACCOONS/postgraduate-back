package com.postgraduate.admin.presentation;

import com.postgraduate.admin.application.dto.req.Login;
import com.postgraduate.admin.application.dto.req.SendMessageRequest;
import com.postgraduate.admin.application.dto.res.*;
import com.postgraduate.admin.application.usecase.*;
import com.postgraduate.global.auth.login.application.dto.res.JwtTokenResponse;
import com.postgraduate.global.auth.login.application.usecase.jwt.JwtUseCase;
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
    private final AdminSeniorUseCase adminSeniorUseCase;
    private final AdminUserUseCase adminUserUseCase;
    private final AdminMentoringUseCase adminMentoringUseCase;
    private final AdminSalaryUseCase adminSalaryUseCase;
    private final AdminPaymentUseCase adminPaymentUseCase;
    private final AdminBatchUseCase adminBatchUseCase;
    private final JwtUseCase jwtUseCase;

    @GetMapping("/loginForm")
    public String loginForm(Model model, Login loginForm) {
        model.addAttribute("loginForm", loginForm);
        return "adminLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Login loginForm, Model model) {
        User user = adminUserUseCase.login(loginForm);
        JwtTokenResponse jwtTokenResponse = jwtUseCase.signIn(user);
        model.addAttribute("tokenResponse", jwtTokenResponse);
        return "adminMain";
    }

    @GetMapping("/seniorInfo")
    public String seniorInfo(Model model) {
        List<SeniorInfo> seniorInfos = adminSeniorUseCase.allSenior();
        model.addAttribute("seniorInfos", seniorInfos);
        return "adminSenior";
    }

    @GetMapping("/certification/{seniorId}")
    public String certification(@PathVariable Long seniorId, Model model) {
        CertificationDetailsResponse certification = adminSeniorUseCase.getCertification(seniorId);
        model.addAttribute("certificationInfo", certification);
        return "seniorCertification";
    }

    @PostMapping("/certification/{seniorId}/refuse")
    public String certificationRefuse(@PathVariable Long seniorId) {
        adminSeniorUseCase.updateNotApprove(seniorId);
        return "adminEmpty";
    }

    @PostMapping("/certification/{seniorId}/approve")
    public String certificationApprove(@PathVariable Long seniorId) {
        adminSeniorUseCase.updateApprove(seniorId);
        return "adminEmpty";
    }

    @GetMapping("/mentoring/{seniorId}")
    public String seniorMentoringInfo(@PathVariable Long seniorId, Model model) {
        MentoringManageResponse mentoringInfos = adminMentoringUseCase.seniorMentorings(seniorId);
        model.addAttribute("mentoringInfos", mentoringInfos);
        return "seniorMentoring";
    }

    @PostMapping("/mentoring/refund/{mentoringId}")
    public String refundMentoring(@AuthenticationPrincipal User user, @PathVariable Long mentoringId) {
        adminMentoringUseCase.refundMentoring(user, mentoringId);
        return "adminEmpty";
    }

    @GetMapping("/salaryInfo")
    public String salaryInfo(Model model) {
        List<SalaryInfoWithOutId> salaryInfos = adminSalaryUseCase.salaryInfos();
        model.addAttribute("salaryInfo", salaryInfos);
        return "adminSalary";
    }

    @GetMapping("/salary/{seniorId}")
    public String seniorSalaryInfo(@PathVariable Long seniorId, Model model) {
        SalaryInfoWithOutId salaryInfo = adminSalaryUseCase.seniorSalary(seniorId);
        model.addAttribute("salaryInfo", salaryInfo);
        return "seniorSalary";
    }

    @PostMapping("/salary/done/{salaryId}")
    public String salaryDone(@PathVariable Long salaryId) {
        adminSalaryUseCase.salaryDone(salaryId);
        return "adminEmpty";
    }

    @GetMapping("/salary/unsettled")
    public String unsettledSalaryInfo(Model model) {
        List<UnSettledSalaryInfo> unSettledSalaryInfos = adminSalaryUseCase.unSettledSalaryInfo();
        model.addAttribute("salaryInfos", unSettledSalaryInfos);
        return "unSettledSalary";
    }

    @PostMapping("/wish/done/{wishId}")
    public String wishDone(@PathVariable Long wishId) {
        adminUserUseCase.wishDone(wishId);
        return "adminEmpty";
    }

    @GetMapping("/user/matching/{userId}")
    public String userMatching(@PathVariable Long userId, Model model) {
        WishResponse wishResponse = adminUserUseCase.wishInfo(userId);
        model.addAttribute("wishInfo", wishResponse);
        return "userWish";
    }

    @GetMapping("/user/mentoring/{userId}")
    public String userMentoring(@PathVariable Long userId, Model model) {
        MentoringManageResponse mentoringInfos = adminMentoringUseCase.userMentoringInfos(userId);
        model.addAttribute("mentoringInfos", mentoringInfos);
        return "userMentoring";
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model) {
        List<UserInfo> userInfos = adminUserUseCase.userInfos();
        model.addAttribute("userInfos", userInfos);
        return "adminUser";
    }

    @GetMapping("/paymentInfo")
    public String paymentInfo(Model model) {
        List<PaymentInfo> paymentInfos = adminPaymentUseCase.paymentInfos();
        model.addAttribute("paymentInfos", paymentInfos);
        return "adminPayment";
    }

    @GetMapping("/payment/mentoring/{paymentId}")
    public String paymentWithMentoring(@PathVariable Long paymentId, Model model) {
        MentoringWithPaymentResponse mentoringWithPaymentResponse = adminPaymentUseCase.paymentMentoringInfo(paymentId);
        model.addAttribute("mentoringInfo", mentoringWithPaymentResponse);
        return "paymentMentoring";
    }

    @PostMapping("/payment/refund/{paymentId}")
    public String refundPayment(@AuthenticationPrincipal User user, @PathVariable Long paymentId) {
        adminPaymentUseCase.refundPayment(user, paymentId);
        return "adminEmpty";
    }

    @GetMapping("/message")
    public String messageManage() {
        return "adminMessage";
    }

    @PostMapping("/message")
    public String sendMessage(@RequestBody SendMessageRequest messageRequest) {
        adminUserUseCase.sendMatchingMessage(messageRequest);
        return "adminEmpty";
    }

    @GetMapping("/batch")
    public String batchManage() {
        return "adminBatch";
    }

    @PostMapping("/batch/salary")
    public String startSalaryBatch() {
        adminBatchUseCase.startSalaryBatch();
        return "adminEmpty";
    }

    @PostMapping("/batch/mentoring/done")
    public String startMentoringDone() {
        adminBatchUseCase.startMentoringDoneBatch();
        return "adminEmpty";
    }

    @PostMapping("/batch/mentoring/cancel")
    public String startMentoringCancel() {
        adminBatchUseCase.startMentoringCancelBatch();
        return "adminEmpty";
    }
}

package com.postgraduate.global.auth.login.application.usecase.oauth;

import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import com.postgraduate.global.auth.login.application.dto.req.SeniorChangeRequest;
import com.postgraduate.global.auth.login.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.global.auth.login.application.dto.req.SignUpRequest;
import com.postgraduate.global.auth.login.util.ProfileUtils;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.member.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.member.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.member.user.application.mapper.UserMapper;
import com.postgraduate.domain.member.user.application.utils.UserUtils;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.service.UserGetService;
import com.postgraduate.domain.member.user.domain.service.UserSaveService;
import com.postgraduate.domain.member.user.domain.service.UserUpdateService;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import com.postgraduate.global.slack.SlackSignUpMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.postgraduate.domain.member.user.application.mapper.UserMapper.mapToRole;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;

@Transactional
@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final ProfileUtils profileUtils;
    private final SalarySaveService salarySaveService;
    private final UserSaveService userSaveService;
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final SeniorSaveService seniorSaveService;
    private final SalaryMapper salaryMapper;
    private final SlackSignUpMessage slackSignUpMessage;
    private final UserUtils userUtils;
    private final SeniorUtils seniorUtils;
    private final BizppurioSeniorMessage bizppurioSeniorMessage;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;
    private Random rd = new Random();

    public User userSignUp(SignUpRequest request) {
        userUtils.checkPhoneNumber(request.phoneNumber());
        User user = UserMapper.mapToUser(request, profileUtils.juniorProfile());
        MemberRole memberRole = mapToRole(USER, user);
        userSaveService.save(user, memberRole);
        if (request.matchingReceive())
            bizppurioJuniorMessage.matchingWaiting(user);
        slackSignUpMessage.sendJuniorSignUp(user);
        return user;
    }

    public void changeUser(User user) {
        if (user.isJunior())
            return;
        MemberRole memberRole = mapToRole(USER, user);
        userUpdateService.addJuniorRole(user, memberRole);
    }

    public User seniorSignUp(SeniorSignUpRequest request) {
        seniorUtils.checkKeyword(request.keyword());
        userUtils.checkPhoneNumber(request.phoneNumber());
        User user = UserMapper.mapToUser(request, profileUtils.seniorProfile(rd.nextInt(5)));
        MemberRole memberRole = mapToRole(SENIOR, user);
        userSaveService.save(user, memberRole);
        Senior senior = SeniorMapper.mapToSenior(user, request);
        return seniorSignUpFin(senior);
    }

    public User changeSenior(User user, SeniorChangeRequest changeRequest) {
        seniorUtils.checkKeyword(changeRequest.keyword());
        Senior senior = SeniorMapper.mapToSenior(user, changeRequest);
        return changeSeniorFin(senior, user);
    }

    private User seniorSignUpFin(Senior senior) {
        seniorSaveService.saveSenior(senior);
        Salary salary = salaryMapper.mapToSalary(senior, getSalaryDate());
        Salary nextSalary = salaryMapper.mapToSalary(senior, getSalaryDate().plusDays(7));
        salarySaveService.save(salary);
        salarySaveService.save(nextSalary);
        slackSignUpMessage.sendSeniorSignUp(senior);
        bizppurioSeniorMessage.signUp(senior.getUser());
        return senior.getUser();
    }

    private User changeSeniorFin(Senior senior, User user) {
        seniorSaveService.saveSenior(senior);
        user = userGetService.byUserId(user.getUserId());
        MemberRole memberRole = mapToRole(SENIOR, user);
        userUpdateService.addSeniorRole(user, rd.nextInt(5), memberRole);
        Salary salary = salaryMapper.mapToSalary(senior, getSalaryDate());
        Salary nextSalary = salaryMapper.mapToSalary(senior, getSalaryDate().plusDays(7));
        salarySaveService.save(salary);
        salarySaveService.save(nextSalary);
        slackSignUpMessage.sendSeniorSignUp(senior);
        bizppurioSeniorMessage.signUp(user);
        return user;
    }
}

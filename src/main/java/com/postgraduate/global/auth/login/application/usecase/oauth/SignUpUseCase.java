package com.postgraduate.global.auth.login.application.usecase.oauth;

import com.postgraduate.global.auth.login.application.dto.req.SeniorChangeRequest;
import com.postgraduate.global.auth.login.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.global.auth.login.application.dto.req.SignUpRequest;
import com.postgraduate.global.auth.login.application.dto.req.UserChangeRequest;
import com.postgraduate.global.auth.login.util.ProfileUtils;
import com.postgraduate.domain.senior.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.senior.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.user.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.user.application.utils.UserUtils;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import com.postgraduate.domain.user.user.domain.service.UserSaveService;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.wish.application.mapper.WishMapper;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import com.postgraduate.domain.user.wish.domain.service.WishSaveService;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import com.postgraduate.global.slack.SlackSignUpMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.postgraduate.domain.senior.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSenior;
import static com.postgraduate.domain.user.user.application.mapper.UserMapper.mapToUser;

@Transactional
@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final ProfileUtils profileUtils;
    private final SalarySaveService salarySaveService;
    private final UserSaveService userSaveService;
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final WishSaveService wishSaveService;
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
        User user = UserMapper.mapToUser(request, profileUtils.juniorProfile(rd.nextInt(5)));
        Wish wish = WishMapper.mapToWish(user, request);
        wishSaveService.save(wish);
        userSaveService.save(user);
        if (request.matchingReceive())
            bizppurioJuniorMessage.matchingWaiting(user);
        slackSignUpMessage.sendJuniorSignUp(user, wish);
        return user;
    }

    public User seniorSignUp(SeniorSignUpRequest request) {
        seniorUtils.checkKeyword(request.keyword());
        userUtils.checkPhoneNumber(request.phoneNumber());
        User user = UserMapper.mapToUser(request, profileUtils.seniorProfile(rd.nextInt(5)));
        userSaveService.save(user);
        Senior senior = SeniorMapper.mapToSenior(user, request);
        return seniorSignUpFin(senior);
    }

    public User changeSenior(User user, SeniorChangeRequest changeRequest) {
        seniorUtils.checkKeyword(changeRequest.keyword());
        Senior senior = SeniorMapper.mapToSenior(user, changeRequest);
        return changeSeniorFin(senior, user);
    }

    public void changeUser(User user, UserChangeRequest changeRequest) {
        Wish wish = WishMapper.mapToWish(user, changeRequest);
        wishSaveService.save(wish);
        if (changeRequest.matchingReceive())
            bizppurioJuniorMessage.matchingWaiting(user);
        slackSignUpMessage.sendJuniorSignUp(user, wish);
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
        userUpdateService.userToSeniorRole(user, rd.nextInt(5));
        Salary salary = salaryMapper.mapToSalary(senior, getSalaryDate());
        Salary nextSalary = salaryMapper.mapToSalary(senior, getSalaryDate().plusDays(7));
        salarySaveService.save(salary);
        salarySaveService.save(nextSalary);
        slackSignUpMessage.sendSeniorSignUp(senior);
        bizppurioSeniorMessage.signUp(user);
        return user;
    }
}

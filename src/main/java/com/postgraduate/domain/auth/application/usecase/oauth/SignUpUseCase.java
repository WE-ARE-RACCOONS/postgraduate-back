package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.dto.req.*;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserSaveService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.wish.application.mapper.WishMapper;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishSaveService;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import com.postgraduate.global.slack.SlackSignUpMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSenior;
import static com.postgraduate.domain.user.application.mapper.UserMapper.mapToUser;

@Transactional
@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    @Value("${profile.default}")
    private String profile;

    private final SalarySaveService salarySaveService;
    private final UserSaveService userSaveService;
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final WishSaveService wishSaveService;
    private final SeniorSaveService seniorSaveService;
    private final SlackSignUpMessage slackSignUpMessage;
    private final UserUtils userUtils;
    private final SeniorUtils seniorUtils;
    private final BizppurioSeniorMessage bizppurioSeniorMessage;

    public User userSignUp(SignUpRequest request) {
        userUtils.checkPhoneNumber(request.phoneNumber());
        User user = mapToUser(request, profile);
        Wish wish = WishMapper.mapToWish(user, request);
        wishSaveService.save(wish);
        userSaveService.save(user);
        slackSignUpMessage.sendJuniorSignUp(user, wish);
        return user;
    }

    public User seniorSignUp(SeniorSignUpRequest request) {
        seniorUtils.checkKeyword(request.keyword());
        userUtils.checkPhoneNumber(request.phoneNumber());
        User user = mapToUser(request, profile);
        userSaveService.save(user);
        Senior senior = mapToSenior(user, request);
        return seniorSignUpFin(senior);
    }

    public User changeSenior(User user, SeniorChangeRequest changeRequest) {
        seniorUtils.checkKeyword(changeRequest.keyword());
        Senior senior = mapToSenior(user, changeRequest);
        return changeSeniorFin(senior, user);
    }

    public void changeUser(User user, UserChangeRequest changeRequest) {
        Wish wish = WishMapper.mapToWish(user, changeRequest);
        wishSaveService.save(wish);
        slackSignUpMessage.sendJuniorSignUp(user, wish);
    }

    private User seniorSignUpFin(Senior senior) {
        seniorSaveService.saveSenior(senior);
        Salary salary = SalaryMapper.mapToSalary(senior, getSalaryDate());
        Salary nextSalary = SalaryMapper.mapToSalary(senior, getSalaryDate().plusDays(7));
        salarySaveService.save(salary);
        salarySaveService.save(nextSalary);
        slackSignUpMessage.sendSeniorSignUp(senior);
        bizppurioSeniorMessage.signUp(senior.getUser());
        return senior.getUser();
    }

    private User changeSeniorFin(Senior senior, User user) {
        seniorSaveService.saveSenior(senior);
        user = userGetService.byUserId(user.getUserId());
        userUpdateService.userToSeniorRole(user);
        Salary salary = SalaryMapper.mapToSalary(senior, getSalaryDate());
        Salary nextSalary = SalaryMapper.mapToSalary(senior, getSalaryDate().plusDays(7));
        salarySaveService.save(salary);
        salarySaveService.save(nextSalary);
        slackSignUpMessage.sendSeniorSignUp(senior);
        bizppurioSeniorMessage.signUp(user);
        return user;
    }
}

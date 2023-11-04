package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileAndAccountRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;

public class SeniorMapper {

    public static Senior mapToSenior(User user, SeniorSignUpRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .certification(request.getCertification())
                .account(mapToAccount(request))
                .build();
    }

    public static Info mapToInfo(SeniorSignUpRequest request) {
        return Info.builder()
                .college(request.getCollege())
                .major(request.getMajor())
                .postgradu(request.getPostgradu())
                .professor(request.getProfessor())
                .lab(request.getLab())
                .field(request.getField())
                .build();
    }

    public static Account mapToAccount(SeniorSignUpRequest request) {
        return Account.builder()
                .bank(request.getBank())
                .account(request.getAccount())
                .rrn(request.getRrn())
                .build();
    }

    public static Account mapToAccount(SeniorProfileAndAccountRequest profileAndAccountPageRequest) {
        return Account.builder()
                .bank(profileAndAccountPageRequest.getBank())
                .account(profileAndAccountPageRequest.getAccount())
                .rrn(profileAndAccountPageRequest.getRrn())
                .build();
    }

    public static Profile mapToProfile(SeniorProfileAndAccountRequest profileAndAccountPageRequest) {
        return Profile.builder()
                .info(profileAndAccountPageRequest.getInfo())
                .chatLink(profileAndAccountPageRequest.getChatLink())
                .target(profileAndAccountPageRequest.getTarget())
                .time(profileAndAccountPageRequest.getTime())
                .build();
    }

    public static Profile mapToProfile(SeniorProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.getInfo())
                .chatLink(profileRequest.getChatLink())
                .target(profileRequest.getTarget())
                .time(profileRequest.getTime())
                .build();
    }

    public static SeniorInfoResponse mapToSeniorInfo(Senior senior, Status certificationRegister, boolean profileRegister) {
        return SeniorInfoResponse.builder()
                .nickName(senior.getUser().getNickName())
                .profile(senior.getUser().getProfile())
                .certificationRegister(certificationRegister)
                .profileRegister(profileRegister)
                .build();
    }

    public static SeniorProfileResponse mapToSeniorProfileInfo(Senior senior) {
        return SeniorProfileResponse.builder()
                .profile(senior.getProfile())
                .account(senior.getAccount())
                .build();
    }
}

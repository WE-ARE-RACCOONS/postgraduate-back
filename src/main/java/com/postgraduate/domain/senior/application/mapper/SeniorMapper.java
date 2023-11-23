package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorDetailResponse;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageProfileResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageUserAccountResponse;
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
                .build();
    }

    public static Info mapToInfo(SeniorSignUpRequest request) {
        return Info.builder()
                .major(request.getMajor())
                .postgradu(request.getPostgradu())
                .professor(request.getProfessor())
                .lab(request.getLab())
                .keyword(request.getKeyword())
                .field(request.getField())
                .build();
    }

    public static Profile mapToProfile(SeniorProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.getInfo())
                .chatLink(profileRequest.getChatLink())
                .oneLiner(profileRequest.getOneLiner())
                .target(profileRequest.getTarget())
                .time(profileRequest.getTime())
                .build();
    }

    public static Senior mapToSenior(User user, SeniorChangeRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .certification(request.getCertification())
                .build();
    }

    public static Info mapToInfo(SeniorChangeRequest request) {
        return Info.builder()
                .major(request.getMajor())
                .postgradu(request.getPostgradu())
                .professor(request.getProfessor())
                .lab(request.getLab())
                .keyword(request.getKeyword())
                .field(request.getField())
                .build();
    }

    public static SeniorMyPageResponse mapToSeniorMyPageInfo(Senior senior, Status certificationRegister, boolean profileRegister) {
        return SeniorMyPageResponse.builder()
                .nickName(senior.getUser().getNickName())
                .profile(senior.getUser().getProfile())
                .certificationRegister(certificationRegister)
                .profileRegister(profileRegister)
                .build();
    }

    public static SeniorMyPageProfileResponse mapToMyPageProfile(Senior senior) {
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        return SeniorMyPageProfileResponse.builder()
                .lab(info.getLab())
                .keyword(info.getKeyword())
                .field(info.getField())
                .info(profile.getInfo())
                .target(profile.getTarget())
                .chatLink(profile.getChatLink())
                .oneLiner(profile.getOneLiner())
                .build();
    }

    public static SeniorMyPageUserAccountResponse mapToMyPageUserAccount(Senior senior, Account account, String accountNumber) {
        User user = senior.getUser();
        return SeniorMyPageUserAccountResponse.builder()
                .profile(user.getProfile())
                .nickName(user.getNickName())
                .bank(account.getBank())
                .accountNumber(accountNumber)
                .accountHolder(account.getAccountHolder())
                .build();
    }

    public static SeniorDetailResponse mapToSeniorDetail(Senior senior) {
        String[] keyword = senior.getInfo().getKeyword().split(",");
        return SeniorDetailResponse.builder()
                .nickName(senior.getUser().getNickName())
                .profile(senior.getUser().getProfile())
                .postgradu(senior.getInfo().getPostgradu())
                .major(senior.getInfo().getMajor())
                .lab(senior.getInfo().getLab())
                .professor(senior.getInfo().getProfessor())
                .keyword(keyword)
                .info(senior.getProfile().getInfo())
                .oneLiner(senior.getProfile().getOneLiner())
                .target(senior.getProfile().getTarget())
                .time(senior.getProfile().getTime())
                .build();
    }
}

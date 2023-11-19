package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
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

    public static SeniorInfoResponse mapToSeniorInfo(Senior senior, Status certificationRegister, boolean profileRegister) {
        return SeniorInfoResponse.builder()
                .nickName(senior.getUser().getNickName())
                .profile(senior.getUser().getProfile())
                .certificationRegister(certificationRegister)
                .profileRegister(profileRegister)
                .build();
    }
}

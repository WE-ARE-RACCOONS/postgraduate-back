package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

public class SeniorMapper {

    public static Senior mapToSenior(User user, SeniorSignUpRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .certification(request.getCertification())
                .account(mapToAccount(request))
                .rrn(request.getRrn())
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
                .build();
    }

    public static SeniorInfoResponse mapToSeniorInfo(Senior senior, boolean certificationRegister, boolean profileRegister) {
        return SeniorInfoResponse.builder()
                .nickName(senior.getUser().getNickName())
                .profile(senior.getUser().getProfile())
                .certificationRegister(certificationRegister)
                .profileRegister(profileRegister)
                .build();
    }
}

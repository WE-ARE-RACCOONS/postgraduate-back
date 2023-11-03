package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

public class SeniorMapper {

    public static Senior mapToSenior(User user, SeniorSignUpRequest request) {
        return Senior.builder()
                .user(user)
                .college(request.getCollege())
                .major(request.getMajor())
                .postgradu(request.getPostgradu())
                .professor(request.getProfessor())
                .lab(request.getLab())
                .field(request.getField())
                .certification(request.getCertification())
                .account(request.getAccount())
                .bank(request.getBank())
                .rrn(request.getRrn())
                .build();
    }
}

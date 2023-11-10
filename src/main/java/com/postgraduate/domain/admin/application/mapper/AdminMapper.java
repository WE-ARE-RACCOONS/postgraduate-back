package com.postgraduate.domain.admin.application.mapper;

import com.postgraduate.domain.admin.application.dto.CertificationInfo;
import com.postgraduate.domain.admin.application.dto.CertificationProfile;
import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.dto.res.UserResponse;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;

public class AdminMapper {

    public static CertificationInfo mapToCertificationInfo(Senior senior) {
        return CertificationInfo.builder()
                .certification(senior.getCertification())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .field(senior.getInfo().getField())
                .professor(senior.getInfo().getProfessor())
                .build();
    }

    public static CertificationProfile mapToCertificationProfile(Senior senior) {
        return CertificationProfile.builder()
                .time(senior.getProfile().getTime())
                .term(senior.getProfile().getTerm())
                .build();
    }

    public static CertificationResponse mapToCertification(Senior senior) {
        return CertificationResponse.builder()
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .createdAt(senior.getCreatedAt())
                .build();
    }


    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .createdAt(user.getCreatedAt())
                .isSenior(user.getRole() == Role.SENIOR)
                .build();
    }
}

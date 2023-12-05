package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Field;
import com.postgraduate.domain.senior.domain.entity.constant.Postgradu;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.HashSet;

public class SeniorMapper {

    public static Senior mapToSenior(User user, SeniorSignUpRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .certification(request.certification())
                .build();
    }

    public static Info mapToInfo(SeniorSignUpRequest request) {
        String[] fields = request.field().split(",");
        HashSet<String> fieldNames = Field.fieldNames();
        HashSet<String> postgraduNames = Postgradu.postgraduNames();

        Info.InfoBuilder infoBuilder = Info.builder()
                .major(request.major())
                .postgradu(request.postgradu())
                .professor(request.professor())
                .lab(request.lab())
                .keyword(request.keyword())
                .field(request.field())
                .etcPostgradu(false)
                .etcField(false)
                .totalInfo(request.major() + request.lab() + request.field()
                        + request.professor() + request.postgradu() + request.keyword());

        for (String field : fields) {
            if (!fieldNames.contains(field)) {
                infoBuilder.etcField(true);
                break;
            }
        }
        if (!postgraduNames.contains(request.postgradu()))
            infoBuilder.etcPostgradu(true);

        return infoBuilder.build();
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

    public static Profile mapToProfile(SeniorMyPageProfileRequest profileRequest) {
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
                .certification(request.certification())
                .build();
    }

    public static Info mapToInfo(SeniorChangeRequest request) {
        String[] fields = request.field().split(",");
        HashSet<String> fieldNames = Field.fieldNames();
        HashSet<String> postgraduNames = Postgradu.postgraduNames();

        Info.InfoBuilder infoBuilder = Info.builder()
                .major(request.major())
                .postgradu(request.postgradu())
                .professor(request.professor())
                .lab(request.lab())
                .keyword(request.keyword())
                .field(request.field())
                .etcPostgradu(false)
                .etcField(false)
                .totalInfo(request.major() + request.lab() + request.field()
                        + request.professor() + request.postgradu() + request.keyword());

        for (String field : fields) {
            if (!fieldNames.contains(field)) {
                infoBuilder.etcField(true);
                break;
            }
        }
        if (!postgraduNames.contains(request.postgradu()))
            infoBuilder.etcPostgradu(true);
        return infoBuilder.build();
    }

    public static SeniorMyPageResponse mapToSeniorMyPageInfo(Senior senior, Status certificationRegister, boolean profileRegister) {
        User user = senior.getUser();
        return new SeniorMyPageResponse(senior.getSeniorId(), user.getNickName(), user.getProfile(), certificationRegister, profileRegister);
    }

    public static SeniorMyPageProfileResponse mapToMyPageProfile(Senior senior) {
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        String[] keyword = info.getKeyword().split(",");
        String[] field = info.getField().split(",");
        return SeniorMyPageProfileResponse.builder()
                .lab(info.getLab())
                .keyword(keyword)
                .field(field)
                .info(profile.getInfo())
                .target(profile.getTarget())
                .chatLink(profile.getChatLink())
                .oneLiner(profile.getOneLiner())
                .time(profile.getTime())
                .build();
    }

    public static SeniorMyPageUserAccountResponse mapToMyPageUserAccount(Senior senior, Account account, String accountNumber) {
        User user = senior.getUser();
        return new SeniorMyPageUserAccountResponse(
                user.getProfile(),
                user.getPhoneNumber(),
                user.getNickName(),
                account.getBank(),
                accountNumber,
                account.getAccountHolder());
    }

    public static SeniorMyPageUserAccountResponse mapToMyPageUserAccount(Senior senior) {
        User user = senior.getUser();
        return new SeniorMyPageUserAccountResponse(
                user.getProfile(),
                user.getPhoneNumber(),
                user.getNickName());
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

    public static SeniorSearchResponse mapToSeniorSearch(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        String[] keyword = info.getKeyword().split(",");

        return new SeniorSearchResponse(senior.getSeniorId(), user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(),
                profile.getOneLiner(), keyword);
    }

    public static SeniorFieldResponse mapToSeniorField(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new SeniorFieldResponse(senior.getSeniorId(), user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab());
    }
}

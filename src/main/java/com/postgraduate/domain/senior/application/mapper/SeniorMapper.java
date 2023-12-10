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

import java.util.Arrays;
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
                .info(profileRequest.info())
                .chatLink(profileRequest.chatLink())
                .oneLiner(profileRequest.oneLiner())
                .target(profileRequest.target())
                .time(profileRequest.time())
                .build();
    }

    public static Profile mapToProfile(SeniorMyPageProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.info())
                .chatLink(profileRequest.chatLink())
                .oneLiner(profileRequest.oneLiner())
                .target(profileRequest.target())
                .time(profileRequest.time())
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
                .etcPostgradu(true)
                .etcField(true)
                .totalInfo(request.major() + request.lab() + request.field()
                        + request.professor() + request.postgradu() + request.keyword());

        for (String field : fields) {
            if (fieldNames.contains(field)) {
                infoBuilder.etcField(false);
                break;
            }
        }
        if (postgraduNames.contains(request.postgradu()))
            infoBuilder.etcPostgradu(false);
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
        return new SeniorMyPageProfileResponse(
                info.getLab(),
                keyword,
                profile.getInfo(),
                profile.getTarget(),
                profile.getChatLink(),
                field,
                profile.getOneLiner(),
                profile.getTime()
        );
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
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        String[] keyword = info.getKeyword().split(",");
        return new SeniorDetailResponse(
                senior.getUser().getNickName(),
                senior.getUser().getProfile(),
                info.getPostgradu(),
                info.getMajor(),
                info.getLab(),
                info.getProfessor(),
                keyword,
                profile.getInfo(),
                profile.getOneLiner(),
                profile.getTarget(),
                profile.getTime()
        );
    }

    public static SeniorSearchResponse mapToSeniorSearch(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        String[] allKeywords = info.getKeyword().split(",");
        String[] keyword = Arrays.copyOf(allKeywords, Math.min(3, allKeywords.length));

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

    public static SeniorProfileResponse mapToSeniorProfile(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new SeniorProfileResponse(user.getNickName(), user.getProfile(),
                info.getPostgradu(), info.getMajor(), info.getLab());
    }
}

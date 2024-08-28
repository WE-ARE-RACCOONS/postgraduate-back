package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.domain.entity.*;
import com.postgraduate.global.auth.login.application.dto.req.SeniorChangeRequest;
import com.postgraduate.global.auth.login.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.res.AvailableTimeResponse;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.domain.entity.constant.Field;
import com.postgraduate.domain.senior.domain.entity.constant.Postgradu;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.user.domain.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.NONE;

public class SeniorMapper {
    private SeniorMapper() {
        throw new IllegalArgumentException();
    }

    public static Senior mapToSenior(User user, SeniorSignUpRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .status(NONE)
                .build();
    }

    public static Senior mapToSenior(User user, SeniorChangeRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .status(NONE)
                .build();
    }

    private static Info mapToInfo(SeniorSignUpRequest request) {
        return Info.builder()
                .major(request.major())
                .postgradu(request.postgradu())
                .professor(request.professor())
                .lab(request.lab())
                .keyword(request.keyword())
                .field(request.field())
                .etcPostgradu(checkEtcPostgradu(request.postgradu()))
                .etcField(checkEtcField(request.field()))
                .chatLink(request.chatLink())
                .totalInfo(request.major() + request.lab() + request.field()
                        + request.professor() + request.postgradu() + request.keyword())
                .build();
    }

    private static Info mapToInfo(SeniorChangeRequest request) {
        return Info.builder()
                .major(request.major())
                .postgradu(request.postgradu())
                .professor(request.professor())
                .lab(request.lab())
                .keyword(request.keyword())
                .field(request.field())
                .etcPostgradu(checkEtcPostgradu(request.postgradu()))
                .etcField(checkEtcField(request.field()))
                .chatLink(request.chatLink())
                .totalInfo(request.major() + request.lab() + request.field()
                        + request.professor() + request.postgradu() + request.keyword())
                .build();
    }

    public static Info mapToInfo(Senior senior, SeniorMyPageProfileRequest request) {
        Info info = senior.getInfo();

        return Info.builder()
                .major(info.getMajor())
                .postgradu(info.getPostgradu())
                .professor(info.getProfessor())
                .lab(request.lab())
                .keyword(request.keyword())
                .field(request.field())
                .etcPostgradu(info.getEtcPostgradu())
                .etcField(checkEtcField(request.field()))
                .chatLink(request.chatLink())
                .totalInfo(info.getMajor() + request.lab() + request.field()
                        + info.getProfessor() + info.getPostgradu() + request.keyword())
                .build();
    }

    private static boolean checkEtcField(String requestField) {
        String[] fields = requestField.split(",");
        Set<String> fieldNames = Field.fieldNames();
        for (String field : fields) {
            if (!fieldNames.contains(field))
                return true;
        }
        return false;
    }

    private static boolean checkEtcPostgradu(String postgradu) {
        Set<String> postgraduNames = Postgradu.postgraduNames();
        return !postgraduNames.contains(postgradu);
    }

    public static Profile mapToProfile(SeniorProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.info())
                .oneLiner(profileRequest.oneLiner())
                .target(profileRequest.target())
                .build();
    }

    public static Profile mapToProfile(SeniorMyPageProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.info())
                .oneLiner(profileRequest.oneLiner())
                .target(profileRequest.target())
                .build();
    }

    public static SeniorMyPageResponse mapToSeniorMyPageInfo(Senior senior, Status certificationRegister, boolean profileRegister) {
        User user = senior.getUser();
        return new SeniorMyPageResponse(user.getSocialId(), senior.getSeniorId(), user.getNickName(), user.getProfile(), certificationRegister, profileRegister);
    }

    public static SeniorMyPageProfileResponse mapToMyPageProfile(Senior senior, List<AvailableTimeResponse> times) {
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        String[] keyword = info.getKeyword().split(",");
        String[] field = info.getField().split(",");
        return new SeniorMyPageProfileResponse(
                info.getLab(),
                keyword,
                profile.getInfo(),
                profile.getTarget(),
                info.getChatLink(),
                field,
                profile.getOneLiner(),
                times
        );
    }

    public static SeniorMyPageProfileResponse mapToMyPageProfile(Senior senior) {
        Info info = senior.getInfo();
        String[] keyword = info.getKeyword().split(",");
        String[] field = info.getField().split(",");
        return new SeniorMyPageProfileResponse(
                info.getLab(),
                keyword,
                null,
                null,
                info.getChatLink(),
                field,
                null,
                null
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

    public static SeniorDetailResponse mapToSeniorDetail(Senior senior, List<AvailableTimeResponse> times, boolean isMine) {
        Info info = senior.getInfo();
        String[] keyword = info.getKeyword().split(",");
        if (senior.getProfile() != null) {
            Profile profile = senior.getProfile();
            return new SeniorDetailResponse(
                    isMine,
                    senior.getStatus().equals(APPROVE),
                    senior.getUser().getNickName(),
                    info.getTerm(),
                    senior.getUser().getProfile(),
                    info.getPostgradu(),
                    info.getMajor(),
                    info.getLab(),
                    info.getProfessor(),
                    keyword,
                    profile.getInfo(),
                    profile.getOneLiner(),
                    profile.getTarget(),
                    times
            );
        }
        return new SeniorDetailResponse(
                isMine,
                senior.getStatus().equals(APPROVE),
                senior.getUser().getNickName(),
                info.getTerm(),
                senior.getUser().getProfile(),
                info.getPostgradu(),
                info.getMajor(),
                info.getLab(),
                info.getProfessor(),
                keyword,
                null,
                null,
                null,
                times
        );
    }

    public static SeniorProfileResponse mapToSeniorProfile(User user, Senior senior) {
        User seniorUser = senior.getUser();
        Info info = senior.getInfo();
        return new SeniorProfileResponse(seniorUser.getNickName(), seniorUser.getProfile(),
                info.getPostgradu(), info.getMajor(), info.getLab(), info.getTerm(), user.getUserId(), user.getPhoneNumber());
    }

    public static SeniorSearchResponse mapToSeniorSearchWithStatus(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        String[] allKeywords = info.getKeyword().split(",");
        String[] keyword = Arrays.copyOf(allKeywords, Math.min(3, allKeywords.length));

        return new SeniorSearchResponse(senior.getSeniorId(), senior.getStatus().equals(APPROVE),user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(), info.getProfessor(),
                keyword);
    }

    public static Available mapToAvailable(Senior senior, AvailableCreateRequest createRequest) {
        return Available.builder()
                .senior(senior)
                .day(createRequest.day())
                .startTime(createRequest.startTime())
                .endTime(createRequest.endTime())
                .build();
    }

    public static AvailableTimeResponse mapToAvailableTimes(Available available) {
        return new AvailableTimeResponse(available.getDay(), available.getStartTime(), available.getEndTime());
    }

    public static Account mapToAccount(Senior senior, SeniorAccountRequest accountRequest, String accountNumber) {
        return Account.builder()
                .senior(senior)
                .accountNumber(accountNumber)
                .accountHolder(accountRequest.accountHolder())
                .bank(accountRequest.bank())
                .build();
    }

    public static Account mapToAccount(Senior senior, SeniorMyPageUserAccountRequest myPageUserAccountRequest, String accountNumber) {
        return Account.builder()
                .senior(senior)
                .accountNumber(accountNumber)
                .accountHolder(myPageUserAccountRequest.accountHolder())
                .bank(myPageUserAccountRequest.bank())
                .build();
    }
}

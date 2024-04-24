package com.postgraduate.domain.senior.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequestB;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequestB;
import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
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
import java.util.List;
import java.util.Set;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.*;

public class SeniorMapper {
    private SeniorMapper() {
        throw new IllegalArgumentException();
    }

    public static Senior mapToSenior(User user, SeniorSignUpRequest request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .certification(request.certification())
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
        Set<String> fieldNames = Field.fieldNames();
        Set<String> postgraduNames = Postgradu.postgraduNames();

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
            if (!fieldNames.contains(field)) {
                infoBuilder.etcField(true);
                break;
            }
        }
        if (!postgraduNames.contains(request.postgradu()))
            infoBuilder.etcPostgradu(true);

        return infoBuilder.build();
    }

    public static Info mapToInfo(SeniorSignUpRequest request) {
        String[] fields = request.field().split(",");
        Set<String> fieldNames = Field.fieldNames();
        Set<String> postgraduNames = Postgradu.postgraduNames();

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

    public static Info mapToInfo(Senior senior, SeniorMyPageProfileRequest request) {
        Info info = senior.getInfo();
        String[] fields = request.field().split(",");
        Set<String> fieldNames = Field.fieldNames();

        Info.InfoBuilder infoBuilder = Info.builder()
                .major(info.getMajor())
                .postgradu(info.getPostgradu())
                .professor(info.getProfessor())
                .lab(request.lab())
                .keyword(request.keyword())
                .field(request.field())
                .etcPostgradu(false)
                .etcField(false)
                .totalInfo(info.getMajor() + request.lab() + request.field()
                        + info.getProfessor() + info.getPostgradu() + request.keyword());

        for (String field : fields) {
            if (!fieldNames.contains(field)) {
                infoBuilder.etcField(true);
                break;
            }
        }
        infoBuilder.etcPostgradu(info.getEtcPostgradu());

        return infoBuilder.build();
    }

    public static Profile mapToProfile(SeniorProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.info())
                .chatLink(profileRequest.chatLink())
                .oneLiner(profileRequest.oneLiner())
                .target(profileRequest.target())
                .build();
    }

    public static Profile mapToProfile(SeniorMyPageProfileRequest profileRequest) {
        return Profile.builder()
                .info(profileRequest.info())
                .chatLink(profileRequest.chatLink())
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
                profile.getChatLink(),
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
                null,
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
        Profile profile = senior.getProfile();
        String[] keyword = info.getKeyword().split(",");
        return new SeniorDetailResponse(
                isMine,
                senior.getUser().getNickName(),
                profile.getTerm(),
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

    public static SeniorSearchResponse mapToSeniorSearch(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        String[] allKeywords = info.getKeyword().split(",");
        String[] keyword = Arrays.copyOf(allKeywords, Math.min(3, allKeywords.length));

        return new SeniorSearchResponse(senior.getSeniorId(), user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(), info.getProfessor(),
                keyword);
    }

    public static SeniorProfileResponse mapToSeniorProfile(User user, Senior senior) {
        User seniorUser = senior.getUser();
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        return new SeniorProfileResponse(seniorUser.getNickName(), seniorUser.getProfile(),
                info.getPostgradu(), info.getMajor(), info.getLab(), profile.getTerm(), user.getUserId(), user.getPhoneNumber());
    }

    /**
     * 여기서부터 Case B 위한 코드
     */

    public static Senior mapToSenior(User user, SeniorSignUpRequestB request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .status(NONE)
                .build();
    }

    public static Senior mapToSenior(User user, SeniorChangeRequestB request) {
        return Senior.builder()
                .user(user)
                .info(mapToInfo(request))
                .status(NONE)
                .build();
    }

    private static Info mapToInfo(SeniorSignUpRequestB request) {
        String[] fields = request.field().split(",");
        Set<String> fieldNames = Field.fieldNames();
        Set<String> postgraduNames = Postgradu.postgraduNames();

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

    private static Info mapToInfo(SeniorChangeRequestB request) {
        String[] fields = request.field().split(",");
        Set<String> fieldNames = Field.fieldNames();
        Set<String> postgraduNames = Postgradu.postgraduNames();

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

    public static SeniorDetailResponseB mapToSeniorDetailWithNull(Senior senior, List<AvailableTimeResponse> times, boolean isMine) {
        Info info = senior.getInfo();
        String[] keyword = info.getKeyword().split(",");
        if (senior.getProfile() != null) {
            Profile profile = senior.getProfile();
            return new SeniorDetailResponseB(
                    isMine,
                    senior.getStatus().equals(APPROVE),
                    senior.getUser().getNickName(),
                    profile.getTerm(),
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
        return new SeniorDetailResponseB(
                isMine,
                senior.getStatus().equals(APPROVE),
                senior.getUser().getNickName(),
                30,
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

    public static SeniorProfileResponse mapToSeniorProfileWithNull(User user, Senior senior) {
        User seniorUser = senior.getUser();
        Info info = senior.getInfo();
        if (senior.getProfile() != null) {
            Profile profile = senior.getProfile();
            return new SeniorProfileResponse(seniorUser.getNickName(), seniorUser.getProfile(),
                    info.getPostgradu(), info.getMajor(), info.getLab(), profile.getTerm(), user.getUserId(), user.getPhoneNumber());
        }
        return new SeniorProfileResponse(seniorUser.getNickName(), seniorUser.getProfile(),
                info.getPostgradu(), info.getMajor(), info.getLab(), 30, user.getUserId(), user.getPhoneNumber());
    }

    public static SeniorSearchResponseB mapToSeniorSearchWithStatus(Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        String[] allKeywords = info.getKeyword().split(",");
        String[] keyword = Arrays.copyOf(allKeywords, Math.min(3, allKeywords.length));

        return new SeniorSearchResponseB(senior.getSeniorId(), senior.getStatus().equals(APPROVE),user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(), info.getProfessor(),
                keyword);
    }
}

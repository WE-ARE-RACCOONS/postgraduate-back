package com.postgraduate.domain.member.senior.application.usecase;

import com.postgraduate.domain.member.senior.application.dto.res.*;
import com.postgraduate.domain.member.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.member.senior.domain.entity.Account;
import com.postgraduate.domain.member.senior.domain.entity.Available;
import com.postgraduate.domain.member.senior.domain.entity.Profile;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.member.senior.application.mapper.SeniorMapper.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeniorMyPageUseCase {
    private final SeniorGetService seniorGetService;
    private final EncryptorUtils encryptorUtils;

    public SeniorMyPageResponse getSeniorMyPage(User user) {
        Senior senior = seniorGetService.byUser(user);
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return mapToSeniorMyPageInfo(senior, senior.getStatus(), profile.isPresent());
    }

    public SeniorMyPageProfileResponse getSeniorMyPageProfile(User user) {
        Senior senior = seniorGetService.byUser(user);
        if (senior.getProfile() == null) {
            return mapToMyPageProfile(senior);
        }
        List<Available> availables = senior.getAvailables();
        List<AvailableTimeResponse> times = availables.stream()
                .map(SeniorMapper::mapToAvailableTimes)
                .toList();
        return mapToMyPageProfile(senior, times);
    }

    public SeniorMyPageUserAccountResponse getSeniorMyPageUserAccount(User user) {
        Senior senior = seniorGetService.byUser(user);
        Optional<Account> optionalAccount = Optional.ofNullable(senior.getAccount());
        if (optionalAccount.isEmpty())
            return mapToMyPageUserAccount(senior);
        Account account = optionalAccount.get();
        String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
        return mapToMyPageUserAccount(senior, account, accountNumber);
    }

    public SeniorPossibleResponse checkUser(User user) {
        if (!user.isJunior())
            return new SeniorPossibleResponse(FALSE, user.getSocialId());
        return new SeniorPossibleResponse(TRUE, user.getSocialId());
    }
}

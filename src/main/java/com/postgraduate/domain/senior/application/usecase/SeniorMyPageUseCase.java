package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageProfileResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageUserAccountResponse;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.exception.NoneAccountException;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.*;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorMyPageUseCase {
    private final SeniorGetService seniorGetService;
    private final AccountGetService accountGetService;
    private final EncryptorUtils encryptorUtils;

    public SeniorMyPageResponse getSeniorInfo(User user) {
        Senior senior = seniorGetService.byUser(user);
        Status status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return mapToSeniorMyPageInfo(senior, status, profile.isPresent());
    }

    public SeniorMyPageProfileResponse getSeniorMyPageProfile(User user) {
        Senior senior = seniorGetService.byUser(user);
        return mapToMyPageProfile(senior);
    }

    public SeniorMyPageUserAccountResponse getSeniorMyPageUserAccount(User user) {
        Senior senior = seniorGetService.byUser(user);
        Account account = accountGetService.bySenior(senior).orElseThrow(NoneAccountException::new);
        String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
        return mapToMyPageUserAccount(senior, account, accountNumber);
    }
}

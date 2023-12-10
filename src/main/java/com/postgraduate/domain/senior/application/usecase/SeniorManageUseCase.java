package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.account.domain.service.AccountSaveService;
import com.postgraduate.domain.account.domain.service.AccountUpdateService;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.postgraduate.domain.account.application.mapper.AccountMapper.mapToAccount;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToProfile;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorManageUseCase {
    private final UserUpdateService userUpdateService;
    private final SeniorUpdateService seniorUpdateService;
    private final SeniorGetService seniorGetService;
    private final AccountGetService accountGetService;
    private final AccountSaveService accountSaveService;
    private final AccountUpdateService accountUpdateService;
    private final EncryptorUtils encryptorUtils;

    public void updateCertification(User user, SeniorCertificationRequest certificationRequest) {
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateCertification(senior, certificationRequest.certification());
    }

    public void signUpProfile(User user, SeniorProfileRequest profileRequest) {
        Senior senior = seniorGetService.byUser(user);
        Profile profile = mapToProfile(profileRequest);
        seniorUpdateService.signUpSeniorProfile(senior, profile);
    }

    public void saveAccount(User user, SeniorAccountRequest accountRequest) {
        Senior senior = seniorGetService.byUser(user);
        String accountNumber = encryptorUtils.encryptData(accountRequest.accountNumber());
        accountSaveService.saveAccount(mapToAccount(senior, accountRequest, accountNumber));
    }

    public void updateSeniorMyPageProfile(User user, SeniorMyPageProfileRequest myPageProfileRequest) {
        Senior senior = seniorGetService.byUser(user);
        Profile profile = mapToProfile(myPageProfileRequest);
        seniorUpdateService.updateMyPageProfile(senior, myPageProfileRequest, profile);
    }

    public void updateSeniorMyPageUserAccount(User user, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        Senior senior = seniorGetService.byUser(user);
        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isEmpty()) {
            updateSeniorMyPageUserAccountNoneAccount(senior, user, myPageUserAccountRequest);
            return;
        }
        Account account = optionalAccount.get();
        String accountNumber = encryptorUtils.encryptData(myPageUserAccountRequest.accountNumber());
        userUpdateService.updateSeniorUserAccount(user.getUserId(), myPageUserAccountRequest);
        accountUpdateService.updateAccount(account, myPageUserAccountRequest, accountNumber);
    }

    private void updateSeniorMyPageUserAccountNoneAccount(Senior senior, User user, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        String accountNumber = encryptorUtils.encryptData(myPageUserAccountRequest.accountNumber());
        Account account = mapToAccount(senior, myPageUserAccountRequest, accountNumber);
        userUpdateService.updateSeniorUserAccount(user.getUserId(), myPageUserAccountRequest);
        accountSaveService.saveAccount(account);
    }
}

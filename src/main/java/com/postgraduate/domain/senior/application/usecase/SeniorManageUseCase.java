package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.account.domain.service.AccountSaveService;
import com.postgraduate.domain.account.domain.service.AccountUpdateService;
import com.postgraduate.domain.senior.application.dto.req.SeniorAccountRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.postgraduate.domain.account.application.mapper.AccountMapper.mapToAccount;

@Service
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
        seniorUpdateService.updateCertification(senior, certificationRequest.getCertification());
    }

    public void updateProfile(User user, SeniorProfileRequest profileRequest) {
        Senior senior = seniorGetService.byUser(user);
        Profile profile = SeniorMapper.mapToProfile(profileRequest);
        seniorUpdateService.updateSeniorProfile(senior, profile);
    }

    public void updateAccount(User user, SeniorAccountRequest accountRequest) {
        Senior senior = seniorGetService.byUser(user);
        Optional<Account> account = accountGetService.bySenior(senior);
        if (account.isEmpty()) {
            String accountNumber = encryptorUtils.encryptData(accountRequest.getAccountNumber());
            String rrn = encryptorUtils.encryptData(accountRequest.getRrn());
            accountSaveService.saveAccount(mapToAccount(senior, accountRequest, accountNumber, rrn));
            return;
        }
        accountUpdateService.updateAccount(account.get(), accountRequest);
    }

    public void updateSeniorMyPageProfile(User user, SeniorMyPageProfileRequest myPageProfileRequest) {
        userUpdateService.updateSeniorMyPage(user.getUserId(), myPageProfileRequest);
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateMyPageProfile(senior, myPageProfileRequest);
    }
}

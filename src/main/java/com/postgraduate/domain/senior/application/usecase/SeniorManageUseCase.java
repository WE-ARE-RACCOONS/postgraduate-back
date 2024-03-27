package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.account.domain.service.AccountSaveService;
import com.postgraduate.domain.account.domain.service.AccountUpdateService;
import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableDeleteService;
import com.postgraduate.domain.available.domain.service.AvailableSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.NoneAccountException;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.account.application.mapper.AccountMapper.mapToAccount;
import static com.postgraduate.domain.available.application.util.AvailableUtil.sortAvailable;
import static com.postgraduate.domain.salary.application.mapper.SalaryMapper.mapToSalaryAccount;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToProfile;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorManageUseCase {
    private final UserUpdateService userUpdateService;
    private final SeniorUpdateService seniorUpdateService;
    private final SeniorGetService seniorGetService;
    private final AvailableSaveService availableSaveService;
    private final AvailableDeleteService availableDeleteService;
    private final AccountGetService accountGetService;
    private final AccountSaveService accountSaveService;
    private final AccountUpdateService accountUpdateService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final EncryptorUtils encryptorUtils;
    private final UserUtils userUtils;
    private final SeniorUtils seniorUtils;

    public void updateCertification(User user, SeniorCertificationRequest certificationRequest) {
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateCertification(senior, certificationRequest.certification());
    }

    public void signUpProfile(User user, SeniorProfileRequest profileRequest) {
        Senior senior = seniorGetService.byUser(user);
        Profile profile = mapToProfile(profileRequest);
        seniorUpdateService.signUpSeniorProfile(senior, profile);
        List<AvailableCreateRequest> availableCreateRequests = profileRequest.times();
        availableDeleteService.delete(senior);
        List<Available> sortedAvailable = sortAvailable(availableCreateRequests, senior);
        sortedAvailable.forEach(availableSaveService::save);
    }

    public void saveAccount(User user, SeniorAccountRequest accountRequest) {
        Senior senior = seniorGetService.byUser(user);
        String accountNumber = encryptorUtils.encryptData(accountRequest.accountNumber());
        Account account = mapToAccount(senior, accountRequest, accountNumber);
        accountSaveService.save(account);
        updateSalaryAccount(senior, account);
    }

    public void updateSeniorMyPageProfile(User user, SeniorMyPageProfileRequest myPageProfileRequest) {
        seniorUtils.checkKeyword(myPageProfileRequest.keyword());
        Senior senior = seniorGetService.byUser(user);
        Profile profile = mapToProfile(myPageProfileRequest);
        seniorUpdateService.updateMyPageProfile(senior, myPageProfileRequest, profile);
        availableDeleteService.delete(senior);
        List<AvailableCreateRequest> availableCreateRequests = myPageProfileRequest.times();
        List<Available> sortedAvailable = sortAvailable(availableCreateRequests, senior);
        sortedAvailable.forEach(availableSaveService::save);
    }

    public void updateSeniorMyPageUserAccount(User user, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        userUtils.checkPhoneNumber(myPageUserAccountRequest.phoneNumber());
        Senior senior = seniorGetService.byUserWithAll(user);
        user = senior.getUser();
        userUpdateService.updateSeniorUserAccount(user, myPageUserAccountRequest);

        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isEmpty()) {
            updateSeniorMyPageUserAccountNoneAccount(senior, myPageUserAccountRequest);
            return;
        }
        if (myPageUserAccountRequest.accountNumber().isEmpty() || myPageUserAccountRequest.accountHolder().isEmpty() || myPageUserAccountRequest.bank().isEmpty())
            throw new NoneAccountException();
        Account account = optionalAccount.get();
        String accountNumber = encryptorUtils.encryptData(myPageUserAccountRequest.accountNumber());
        accountUpdateService.updateAccount(account, myPageUserAccountRequest, accountNumber);
        updateSalaryAccount(senior, account);
    }

    private void updateSeniorMyPageUserAccountNoneAccount(Senior senior, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        if (myPageUserAccountRequest.accountNumber().isEmpty() || myPageUserAccountRequest.accountHolder().isEmpty() || myPageUserAccountRequest.bank().isEmpty()) {
            return;
        }
        String accountNumber = encryptorUtils.encryptData(myPageUserAccountRequest.accountNumber());
        Account account = mapToAccount(senior, myPageUserAccountRequest, accountNumber);
        accountSaveService.save(account);
        updateSalaryAccount(senior, account);
    }

    private void updateSalaryAccount(Senior senior, Account account) {
        List<Salary> salaries = salaryGetService.allBySeniorAndAccountIsNull(senior);
        SalaryAccount salaryAccount = mapToSalaryAccount(account);
        salaries.forEach(salary -> salaryUpdateService.updateAccount(salary, salaryAccount));
    }
}

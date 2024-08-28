package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.senior.domain.entity.Available;
import com.postgraduate.domain.senior.domain.service.SeniorDeleteService;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileUpdateResponse;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.NoneAccountException;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import com.postgraduate.global.slack.SlackCertificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.*;
import static com.postgraduate.domain.senior.application.utils.AvailableUtil.sortAvailable;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorManageUseCase {
    private final UserUpdateService userUpdateService;
    private final SeniorUpdateService seniorUpdateService;
    private final SeniorGetService seniorGetService;
    private final SeniorSaveService seniorSaveService;
    private final SeniorDeleteService seniorDeleteService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final SalaryMapper salaryMapper;
    private final EncryptorUtils encryptorUtils;
    private final UserUtils userUtils;
    private final SeniorUtils seniorUtils;
    private final SlackCertificationMessage slackCertificationMessage;

    public void updateCertification(User user, SeniorCertificationRequest certificationRequest) {
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateCertification(senior, certificationRequest.certification());
        slackCertificationMessage.sendCertification(senior);
    }

    public SeniorProfileUpdateResponse signUpProfile(User user, SeniorProfileRequest profileRequest) {
        Senior senior = seniorGetService.byUser(user);
        Profile profile = mapToProfile(profileRequest);
        seniorUpdateService.signUpSeniorProfile(senior, profile);
        List<AvailableCreateRequest> availableCreateRequests = profileRequest.times();
        seniorDeleteService.deleteAvailable(senior);
        List<Available> sortedAvailable = sortAvailable(availableCreateRequests, senior);
        seniorSaveService.saveAllAvailable(senior, sortedAvailable);
        return new SeniorProfileUpdateResponse(senior.getSeniorId());
    }

    public void saveAccount(User user, SeniorAccountRequest accountRequest) {
        Senior senior = seniorGetService.byUser(user);
        String accountNumber = encryptorUtils.encryptData(accountRequest.accountNumber());
        Account account = mapToAccount(senior, accountRequest, accountNumber);
        seniorSaveService.saveAccount(account);
        updateSalaryAccount(senior, account);
    }

    public SeniorProfileUpdateResponse updateSeniorMyPageProfile(User user, SeniorMyPageProfileRequest myPageProfileRequest) {
        seniorUtils.checkKeyword(myPageProfileRequest.keyword());
        Senior senior = seniorGetService.byUser(user);
        Profile profile = mapToProfile(myPageProfileRequest);
        Info info = mapToInfo(senior, myPageProfileRequest);
        seniorUpdateService.updateMyPageProfile(senior, info, profile);
        seniorDeleteService.deleteAvailable(senior);
        List<AvailableCreateRequest> availableCreateRequests = myPageProfileRequest.times();
        List<Available> sortedAvailable = sortAvailable(availableCreateRequests, senior);
        seniorSaveService.saveAllAvailable(senior, sortedAvailable);
        return new SeniorProfileUpdateResponse(senior.getSeniorId());
    }

    public void updateSeniorMyPageUserAccount(User user, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        userUtils.checkPhoneNumber(myPageUserAccountRequest.phoneNumber());
        Senior senior = seniorGetService.byUserWithAll(user);
        user = senior.getUser();
        userUpdateService.updateSeniorUserAccount(user, myPageUserAccountRequest);

        Optional<Account> optionalAccount = Optional.ofNullable(senior.getAccount());
        if (optionalAccount.isEmpty()) {
            updateSeniorMyPageUserAccountNoneAccount(senior, myPageUserAccountRequest);
            return;
        }
        if (myPageUserAccountRequest.accountNumber().isEmpty() || myPageUserAccountRequest.accountHolder().isEmpty() || myPageUserAccountRequest.bank().isEmpty())
            throw new NoneAccountException();
        Account account = optionalAccount.get();
        String accountNumber = encryptorUtils.encryptData(myPageUserAccountRequest.accountNumber());
        seniorUpdateService.updateAccount(senior, myPageUserAccountRequest, accountNumber);
        updateSalaryAccount(senior, account);
    }

    private void updateSeniorMyPageUserAccountNoneAccount(Senior senior, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        if (myPageUserAccountRequest.accountNumber().isEmpty() || myPageUserAccountRequest.accountHolder().isEmpty() || myPageUserAccountRequest.bank().isEmpty()) {
            return;
        }
        String accountNumber = encryptorUtils.encryptData(myPageUserAccountRequest.accountNumber());
        Account account = mapToAccount(senior, myPageUserAccountRequest, accountNumber);
        seniorSaveService.saveAccount(account);
        updateSalaryAccount(senior, account);
    }

    private void updateSalaryAccount(Senior senior, Account account) {
        List<Salary> salaries = salaryGetService.allBySeniorAndAccountIsNull(senior);
        SalaryAccount salaryAccount = salaryMapper.mapToSalaryAccount(account);
        salaries.forEach(salary -> salaryUpdateService.updateAccount(salary, salaryAccount));
    }
}

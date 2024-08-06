package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
import com.postgraduate.domain.available.application.mapper.AvailableMapper;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableGetService;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeniorMyPageUseCase {
    private final SeniorGetService seniorGetService;
    private final AvailableGetService availableGetService;
    private final AccountGetService accountGetService;
    private final EncryptorUtils encryptorUtils;
    private final WishGetService wishGetService;

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
        List<Available> availables = availableGetService.byMine(senior);
        List<AvailableTimeResponse> times = availables.stream()
                .map(AvailableMapper::mapToAvailableTimes)
                .toList();
        return mapToMyPageProfile(senior, times);
    }

    public SeniorMyPageUserAccountResponse getSeniorMyPageUserAccount(User user) {
        Senior senior = seniorGetService.byUser(user);
        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isEmpty())
            return mapToMyPageUserAccount(senior);
        Account account = optionalAccount.get();
        String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
        return mapToMyPageUserAccount(senior, account, accountNumber);
    }

    public SeniorPossibleResponse checkUser(User user) {
        Optional<Wish> wish = wishGetService.byUser(user);
        if (wish.isEmpty())
            return new SeniorPossibleResponse(FALSE, user.getSocialId());
        return new SeniorPossibleResponse(TRUE, user.getSocialId());
    }
}

package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SeniorUpdateService {

    public void updateSeniorProfile(Senior senior, Profile profile) {
        senior.updateProfile(profile);
    }

    public void updateSeniorProfileAndAccount(Senior senior, Profile profile, Account account) {
        senior.updateProfileAndAccount(profile, account);
    }

    public void updateCertification(Senior senior, String imageUrl) {
        senior.updateCertification(imageUrl);
    }
}

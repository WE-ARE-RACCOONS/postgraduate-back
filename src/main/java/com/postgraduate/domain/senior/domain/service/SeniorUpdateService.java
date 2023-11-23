package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SeniorUpdateService {

    public void updateSeniorProfile(Senior senior, Profile profile) {
        senior.signUpProfile(profile);
    }

    public void updateCertification(Senior senior, String imageUrl) {
        senior.updateCertification(imageUrl);
    }

    public void updateMyPageProfile(Senior senior, SeniorMyPageProfileRequest myPageProfileRequest) {
        senior.updateMyPageProfile(myPageProfileRequest);
    }

    public void updateMyPageUserAccount(Senior senior, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {

    }

    public void updateHit(Senior senior) {
        senior.updateHit();
    }
}

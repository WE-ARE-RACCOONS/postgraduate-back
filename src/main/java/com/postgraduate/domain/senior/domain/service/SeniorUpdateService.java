package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorUpdateService {
    public void signUpSeniorProfile(Senior senior, Profile profile) {
        senior.updateProfile(profile);
    }

    public void updateCertification(Senior senior, String imageUrl) {
        senior.updateCertification(imageUrl);
    }

    public void updateMyPageProfile(Senior senior, SeniorMyPageProfileRequest myPageProfileRequest, Profile profile) {
        senior.updateProfile(profile);
        senior.updateInfo(myPageProfileRequest);
    }

    public void updateHit(Senior senior) {
        senior.updateHit();
    }

    public void updateCertificationStatus(Senior senior, Status status) {
        senior.updateStatus(status);
    }
}

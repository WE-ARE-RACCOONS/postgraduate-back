package com.postgraduate.domain.senior.domain.service;

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

    public void updateCertification(Senior senior, String imageUrl) {
        senior.updateCertification(imageUrl);
    }
}

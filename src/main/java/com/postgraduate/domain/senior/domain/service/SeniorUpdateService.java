package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.*;

@Service
@RequiredArgsConstructor
public class SeniorUpdateService {
    public void signUpSeniorProfile(Senior senior, Profile profile) {
        senior.updateProfile(profile);
    }

    public void updateCertification(Senior senior, String imageUrl) {
        senior.updateCertification(imageUrl);
        senior.updateStatus(WAITING);
    }

    public void updateMyPageProfile(Senior senior, Info info, Profile profile) {
        senior.updateProfile(profile);
        senior.updateInfo(info);
    }

    public void plusMentoring(Senior senior) {
        senior.plusMentoringHit();
    }

    public void minusMentoring(Senior senior) {
        senior.minusMentoringHit();
    }

    public void updateHit(Senior senior) {
        senior.updateHit();
    }
}

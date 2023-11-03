package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SeniorUpdateService {

    public void updateSeniorProfile(Senior senior, SeniorProfileRequest profileRequest) {
        senior.updateProfile(profileRequest);
    }

    public void updateCertification(Senior senior, String imageUrl) {
        senior.updateCertification(imageUrl);
    }
}

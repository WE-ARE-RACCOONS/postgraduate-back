package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.repository.SeniorRepository;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorSaveService {
    private final SeniorRepository seniorRepository;

    public Senior saveSenior(User user, SeniorSignUpRequest request) {
        Senior senior = SeniorMapper.mapToSenior(user, request);
        return seniorRepository.save(senior);
    }
}

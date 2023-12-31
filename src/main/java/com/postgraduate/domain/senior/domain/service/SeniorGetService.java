package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.senior.exception.NoneSeniorException;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
public class SeniorGetService {
    private final SeniorRepository seniorRepository;
    private static final int SENIOR_PAGE_SIZE = 10;
    private static final int ADMIN_PAGE_SIZE = 15;

    public Senior byUser(User user) {
        return seniorRepository.findByUser(user).orElseThrow(NoneSeniorException::new);
    }

    public Senior bySeniorId(Long seniorId) {
        return seniorRepository.findBySeniorIdAndUser_IsDelete(seniorId, FALSE)
                .orElseThrow(NoneSeniorException::new);
    }

    public Senior bySeniorIdWithCertification(Long seniorId) {
        return seniorRepository.findBySeniorId(seniorId)
                .orElseThrow(NoneSeniorException::new);
    }

    public Page<Senior> all(Integer page, String search) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return seniorRepository.findAllBySearchSeniorWithAdmin(search, pageable);
    }

    public Page<Senior> bySearch(String search, Integer page, String sort) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, SENIOR_PAGE_SIZE);
        return seniorRepository.findAllBySearchSenior(search, sort, pageable);
    }

    public Page<Senior> byField(String field, String postgradu, Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, SENIOR_PAGE_SIZE);
        Page<Senior> allByFieldSenior = seniorRepository.findAllByFieldSenior(field, postgradu, pageable);
        return allByFieldSenior;
    }
}

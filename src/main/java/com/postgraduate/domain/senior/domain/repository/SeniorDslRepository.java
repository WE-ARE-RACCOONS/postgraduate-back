package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SeniorDslRepository {
    Optional<Senior> findBySeniorId(Long seniorId);
    Optional<Senior> findByUserWithAll(User user);
    List<Senior> findAllSenior();
    Page<Senior> findAllByFieldSenior(String field, String postgradu, Pageable pageable);
    Page<Senior> findAllBySearchSenior(String search, String sort, Pageable pageable);
    List<Available> findAllAvailableBySenior(Senior senior);
    void deleteAvailableBySenior(Senior senior);
    void saveAvailable(Available available);
    void saveAccount(Account account);
    void deleteAccount(Senior senior);
}

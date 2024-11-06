package com.postgraduate.domain.member.senior.domain.service;

import com.postgraduate.domain.member.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.member.senior.domain.entity.Account;
import com.postgraduate.domain.member.senior.domain.entity.Available;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeniorSaveService {
    private final SeniorRepository seniorRepository;

    public Senior saveSenior(Senior senior) {
        return seniorRepository.save(senior);
    }
    public void saveAllAvailable(Senior senior, List<Available> availables) {
        availables.forEach(seniorRepository::saveAvailable);
        senior.addAvailable(availables);
    }

    public void saveAccount(Account account) {
        seniorRepository.saveAccount(account);
    }
}

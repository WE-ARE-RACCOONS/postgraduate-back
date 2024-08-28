package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
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

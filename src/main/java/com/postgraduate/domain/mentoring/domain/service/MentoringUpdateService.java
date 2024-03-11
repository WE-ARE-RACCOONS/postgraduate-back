package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringUpdateService {

    public void updateStatus(Mentoring mentoring, Status status) {
        mentoring.updateStatus(status);
    }

    public void updateDone(Mentoring mentoring, Salary salary) {
        mentoring.updateDone(salary);
    }

    public void updateDate(Mentoring mentoring, String date) {
        mentoring.updateDate(date);
    }
}

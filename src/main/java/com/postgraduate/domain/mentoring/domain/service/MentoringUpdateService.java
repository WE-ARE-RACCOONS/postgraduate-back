package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@RequiredArgsConstructor
public class MentoringUpdateService {

    public void updateStatus(Mentoring mentoring, Status status) {
        mentoring.updateStatus(status);
    }

    public void updateRefuse(Mentoring mentoring) {
        mentoring.updateStatus(REFUSE);
    }

    public void updateCancel(Mentoring mentoring) {
        mentoring.updateStatus(CANCEL);
    }

    public void updateExpected(Mentoring mentoring, String date) {
        mentoring.updateDate(date);
        mentoring.updateStatus(EXPECTED);
    }

    public void updateDone(Mentoring mentoring, Salary salary) {
        mentoring.updateStatus(DONE);
        mentoring.updateSalary(salary);
    }
}

package com.postgraduate.global.auth.quit.application.utils;

import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.exception.IncompleteJuniorMentoringException;
import com.postgraduate.domain.member.user.exception.IncompleteSalaryException;
import com.postgraduate.domain.member.user.exception.IncompleteSeniorMentoringException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuitUtils {
    private final MentoringRepository mentoringRepository;
    private final SalaryRepository salaryRepository;

    public void checkDeleteCondition(Senior senior) {
        if (mentoringRepository.existSeniorMentoring(senior))
            throw new IncompleteSeniorMentoringException();
        if (mentoringRepository.existUserMentoring(senior.getUser()))
            throw new IncompleteJuniorMentoringException();
        if (salaryRepository.existIncompleteSalary(senior))
            throw new IncompleteSalaryException();
    }

    public void checkDeleteCondition(User user) {
        if (mentoringRepository.existUserMentoring(user))
            throw new IncompleteJuniorMentoringException();
    }
}

package com.postgraduate.domain.salary.util;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.*;

@Slf4j
public class SalaryUtil {
    private static final int SALARY_DATE = 4; // 목요일
    private static final int SALARY_END_DATE = 7; // 일요일
    private static final int ONE_WEEK = 7;
    private static final int ZERO = 0;

    private SalaryUtil() {
        throw new IllegalArgumentException();
    }

    public static LocalDate getSalaryDate() {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getValue() == SALARY_END_DATE
                ? now.plusDays(ONE_WEEK + SALARY_DATE)
                : now.plusDays(ONE_WEEK + (SALARY_DATE - dayOfWeek.getValue()));
    }

    public static SalaryStatus getStatus(Salary salary) {
        if (salary.getTotalAmount() == ZERO) {
            return NONE;
        }
        if (salary.status())
            return DONE;
        return YET;
    }
}

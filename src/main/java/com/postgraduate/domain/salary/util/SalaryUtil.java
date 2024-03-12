package com.postgraduate.domain.salary.util;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.entity.Salary;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.*;

public class SalaryUtil {
    private static final int SALARY_DATE = 4; // 목요일
    private static final int SALARY_END_DATE = 7; // 일요일

    private SalaryUtil() {
        throw new IllegalArgumentException();
    }

    public static LocalDate getSalaryDate() {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getValue() == SALARY_END_DATE
                ? now.plusDays(7 + (SALARY_DATE - dayOfWeek.getValue()))
                : now.plusDays(7 + SALARY_DATE);
    }

    public static SalaryStatus getStatus(Salary salary) {
        if (salary.getTotalAmount() == 0) {
            return NONE;
        }
        if (salary.status())
            return DONE;
        return YET;
    }
}

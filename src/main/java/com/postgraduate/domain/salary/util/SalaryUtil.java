package com.postgraduate.domain.salary.util;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.*;

public class SalaryUtil {
    private static final int SALARY_DATE = 10;
    public static LocalDate getSalaryDate() {
        LocalDate now = LocalDate.now();
        return now.getDayOfMonth() <= SALARY_DATE
                ? now.withDayOfMonth(SALARY_DATE)
                : now.plusMonths(1).withDayOfMonth(SALARY_DATE);
    }

    public static DateTimeFormatter getMonthFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM");
    }

    public static SalaryStatus getStatus(Salary salary) {
        if (salary.getTotalAmount() == 0) {
            return NONE;
        }
        if (salary.getStatus())
            return DONE;
        return YET;
    }
}

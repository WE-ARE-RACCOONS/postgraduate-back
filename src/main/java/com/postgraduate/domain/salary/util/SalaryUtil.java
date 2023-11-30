package com.postgraduate.domain.salary.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
}

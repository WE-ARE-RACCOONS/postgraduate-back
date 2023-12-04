package com.postgraduate.domain.salary.util;

import com.postgraduate.domain.salary.domain.entity.Salary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public static int getAmount(List<Salary> salaries) {
        return salaries.stream()
                .map(salary -> salary.getMentoring().getPay())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static Boolean getStatus(List<Salary> salaries) {
        long count = salaries.stream()
                .filter(Salary::getStatus)
                .count();
        if (count == salaries.size()) {
            return true;
        }
        if (count != salaries.size()) {
            return false;
        }
        return null;
    }
}

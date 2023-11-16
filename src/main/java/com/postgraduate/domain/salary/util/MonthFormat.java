package com.postgraduate.domain.salary.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class MonthFormat {
    public static String getMonthFormat(LocalDate date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(date);
    }
}

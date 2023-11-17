package com.postgraduate.domain.salary.util;

import java.time.format.DateTimeFormatter;

public class MonthFormat {
    public static DateTimeFormatter getMonthFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM");
    }
}

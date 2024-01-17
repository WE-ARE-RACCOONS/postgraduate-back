package com.postgraduate.domain.mentoring.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class DateUtils {
    public static LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd-HH-mm");
        return LocalDateTime.parse(date, formatter);
    }

    public static String remainTime(Duration duration) {
        long minutes = duration.toMinutes();
        long hours = minutes / 60;
        minutes -= 60*hours;
        return (hours + "-" + minutes);
    }
}

package com.postgraduate.domain.mentoring.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class DateUtils {
    private DateUtils() {
        throw new IllegalArgumentException();
    }
    
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

    public static String mentoringDateToTime(String date) {
        String[] split = date.split("-");
        return split[1] + "월 " + split[2] + "일 " + split[3] + "시 " + split[4] + "분";
    }
}

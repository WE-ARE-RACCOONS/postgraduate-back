package com.postgraduate.domain.member.senior.application.utils;

import com.postgraduate.domain.member.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.member.senior.domain.entity.Available;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.exception.DayAvailableException;
import com.postgraduate.domain.member.senior.application.dto.req.AvailableCreateRequest;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AvailableUtil {
    public static List<Available> sortAvailable(List<AvailableCreateRequest> availableCreateRequests, Senior senior) {
        return availableCreateRequests.stream()
                .sorted(
                        Comparator.comparing(
                                (AvailableCreateRequest availableCreateRequest) -> {
                                    String day = availableCreateRequest.day();
                                    return Day.sortDay(day);
                                }
                        ).thenComparingInt(
                                availableCreateRequest -> {
                                    String startTime = availableCreateRequest.startTime();
                                    String time = startTime.substring(0, 2);
                                    return Integer.parseInt(time);
                                }
                        )
                )
                .map(availableCreateRequest -> SeniorMapper.mapToAvailable(senior, availableCreateRequest))
                .toList();
    }

    @AllArgsConstructor
    private enum Day {
        월(0), 화(1), 수(2), 목(3), 금(4), 토(5), 일(6);
        private final int dayNum;

        private static int sortDay(String inputDay) {
            return Arrays.stream(Day.values())
                    .filter(day -> day.name().equals(inputDay))
                    .findFirst()
                    .orElseThrow(DayAvailableException::new)
                    .dayNum;
        }
    }
}

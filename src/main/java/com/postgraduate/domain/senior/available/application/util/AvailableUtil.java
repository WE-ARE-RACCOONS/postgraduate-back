package com.postgraduate.domain.senior.available.application.util;

import com.postgraduate.domain.senior.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.senior.available.domain.entity.Available;
import com.postgraduate.domain.senior.available.exception.DayAvailableException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.postgraduate.domain.senior.available.application.mapper.AvailableMapper.mapToAvailable;

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
                .map(availableCreateRequest -> mapToAvailable(senior, availableCreateRequest))
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

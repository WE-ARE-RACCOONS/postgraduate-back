package com.postgraduate.domain.available.application.util;

import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;

import java.util.Comparator;
import java.util.List;

import static com.postgraduate.domain.available.application.mapper.AvailableMapper.mapToAvailable;

public class AvailableUtil {
    public static List<Available> sortAvailable(List<AvailableCreateRequest> availableCreateRequests, Senior senior) {
        List<Available> sortedAvailable = availableCreateRequests.stream()
                .sorted(Comparator.comparing(
                        availableCreateRequest -> {
                            String day = availableCreateRequest.day();
                            if (day.equals("월"))
                                return 0;
                            if (day.equals("화"))
                                return 1;
                            if (day.equals("수"))
                                return 2;
                            if (day.equals("목"))
                                return 3;
                            if (day.equals("금"))
                                return 4;
                            if (day.equals("토"))
                                return 5;
                            if (day.equals("일"))
                                return 6;
                            throw new IllegalArgumentException();
                        }
                ))
                .map(availableCreateRequest -> mapToAvailable(senior, availableCreateRequest))
                .toList();
        return sortedAvailable;
    }
}

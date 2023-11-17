package com.postgraduate.domain.refuse.application.mapper;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.domain.entity.Refuse;

public class RefuseMapper {
    public static Refuse mapToRefuse(Mentoring mentoring, MentoringRefuseRequest request) {
        return Refuse.builder()
                .mentoring(mentoring)
                .reason(request.getReason())
                .build();
    }
}

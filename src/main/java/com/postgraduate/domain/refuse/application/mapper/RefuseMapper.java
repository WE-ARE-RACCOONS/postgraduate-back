package com.postgraduate.domain.refuse.application.mapper;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.domain.entity.Refuse;

public class RefuseMapper {
    private RefuseMapper() {
        throw new IllegalArgumentException();
    }
    
    private static final String AUTO_CANCEL = "자동취소";
    public static Refuse mapToRefuse(Mentoring mentoring, MentoringRefuseRequest request) {
        return Refuse.builder()
                .mentoring(mentoring)
                .reason(request.reason())
                .build();
    }

    public static Refuse mapToRefuse(Mentoring mentoring) {
        return Refuse.builder()
                .mentoring(mentoring)
                .reason(AUTO_CANCEL)
                .build();
    }
}

package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Info {
    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String postgradu;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private String lab;

    @Column(nullable = false)
    private String field;

    public void updateMyPage(SeniorMyPageProfileRequest request) {
        this.keyword = request.getKeyword();
        this.lab = request.getLab();
        this.field = request.getField();
    }
}

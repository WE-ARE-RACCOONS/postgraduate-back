package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.domain.entity.constant.Field;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @Column(nullable = false)
    @Builder.Default
    private Boolean etcField = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean etcPostgradu = false;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String totalInfo; // 모든 Info정보 String으로 가지는 컬럼 - 검색시 사용

    public void updateMyPage(SeniorMyPageProfileRequest request) {
        String[] fileds = request.field().split(",");
        Set<String> fieldNames = Field.fieldNames();
        this.keyword = request.keyword();
        this.lab = request.lab();
        this.field = request.field();
        for (String newField : fileds) {
            if (fieldNames.contains(newField)) {
                this.etcField = false;
                return;
            }
            this.etcField = true;
        }
        combineTotalInfo();
    }

    private void combineTotalInfo() {
        this.totalInfo = major + lab + field + professor + postgradu + keyword;
    }
}

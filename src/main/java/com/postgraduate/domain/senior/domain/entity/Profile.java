package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
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
public class Profile {
    private String info;

    private String oneLiner;

    private String target;

    private String chatLink;

    private String time;

    @Builder.Default
    private Integer term = 40;

    public void updateMyPage(SeniorMyPageProfileRequest request) {
        this.info = request.getInfo();
        this.oneLiner = request.getOneLiner();
        this.target = request.getTarget();
        this.chatLink = request.getChatLink();
    }
}

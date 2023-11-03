package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Profile {
    private String info;

    private String target;

    private String chatLink;

    private String time;

    private Integer term;

    public void updateProfile(SeniorProfileRequest profileRequest) {
        this.info = profileRequest.getInfo();
        this.target = profileRequest.getTarget();
        this.chatLink = profileRequest.getChatLink();
        this.time = profileRequest.getTime();
    }
}

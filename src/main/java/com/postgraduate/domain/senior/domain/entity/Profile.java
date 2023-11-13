package com.postgraduate.domain.senior.domain.entity;

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

    private String keyword;

    private String target;

    private String chatLink;

    private String time;

    @Builder.Default
    private Integer term = 40;
}

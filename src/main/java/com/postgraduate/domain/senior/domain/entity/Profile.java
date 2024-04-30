package com.postgraduate.domain.senior.domain.entity;

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
public class Profile {
    @Column(columnDefinition = "TEXT")
    private String info;

    private String oneLiner;

    @Column(columnDefinition = "TEXT")
    private String target;
}

package com.postgraduate.domain.senior.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Account {
    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String bank;
}

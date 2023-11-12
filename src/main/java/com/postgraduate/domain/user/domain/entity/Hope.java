package com.postgraduate.domain.user.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class Hope {
    private String major;
    private String field;
    private Boolean receive;

    public Hope() {
        this.receive = false;
    }
}

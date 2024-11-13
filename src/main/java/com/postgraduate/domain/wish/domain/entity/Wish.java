package com.postgraduate.domain.wish.domain.entity;

import com.postgraduate.domain.member.user.domain.entity.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    private String major;

    private String field;

    @Column(nullable = false)
    private Boolean matchingReceive;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    protected void updateDone() {
        this.status = Status.MATCHED;
    }
}

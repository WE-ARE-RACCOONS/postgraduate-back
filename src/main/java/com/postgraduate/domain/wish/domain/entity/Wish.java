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

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String postgradu;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private String lab;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.WAITING;

    protected void updateDone() {
        this.status = Status.MATCHED;
    }
}

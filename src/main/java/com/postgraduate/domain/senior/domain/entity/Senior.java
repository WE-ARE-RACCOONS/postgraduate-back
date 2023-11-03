package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.user.domain.entity.User;
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
public class Senior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seniorId;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String postgradu;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private String lab;

    @Column(nullable = false)
    private String field;

    private String info;

    private String target;

    private String chatLink;

    private String time;

    private int term;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String certification;

    @Column(nullable = false)
    private String rrn;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private int hit;

    public void updateProfile(SeniorProfileRequest profileRequest) {
        this.info = profileRequest.getInfo();
        this.target = profileRequest.getTarget();
        this.chatLink = profileRequest.getChatLink();
        this.time = profileRequest.getTime();
    }
}

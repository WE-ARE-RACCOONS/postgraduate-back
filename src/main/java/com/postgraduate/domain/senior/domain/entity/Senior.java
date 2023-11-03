package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@DynamicInsert
public class Senior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seniorId;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private String certification;

    @Column(nullable = false)
    private String rrn;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status = false;

    @Column(nullable = false)
    private int hit;

    @Embedded
    private Info info;

    @Embedded
    private Account account;

    @Embedded
    private Profile profile;
    public void updateProfile(SeniorProfileRequest profileRequest) {
        profile.updateProfile(profileRequest);
    }

    public void updateCertification(String certification) {
        this.certification = certification;
        this.status = false;
    }
}

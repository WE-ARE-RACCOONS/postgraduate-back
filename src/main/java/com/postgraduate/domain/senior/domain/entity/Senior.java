package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;

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
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = WAITING;

    @Column(nullable = false)
    private int hit;

    @Embedded
    private Info info;

    @Embedded
    private Profile profile;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    public void updateProfile(Profile profile) {
        this.profile = profile;
    }

    public void updateMyPage(SeniorMyPageProfileRequest myPageProfileRequest) {
        info.updateMyPage(myPageProfileRequest);
        profile.updateMyPage(myPageProfileRequest);
    }

    public void updateCertification(String certification) {
        this.certification = certification;
        this.status = WAITING;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateHit() {
        this.hit++;
    }
}

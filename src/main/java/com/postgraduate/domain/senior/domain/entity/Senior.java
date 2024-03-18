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

import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
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
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateProfile(Profile profile) {
        this.profile = profile;
    }

    public void updateInfo(SeniorMyPageProfileRequest myPageProfileRequest) {
        this.info.updateMyPage(myPageProfileRequest);
    }

    public void updateCertification(String certification) {
        this.certification = certification;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateHit() {
        this.hit++;
    }

    public boolean isEqual(Senior senior) {
        return senior.getSeniorId()
                .equals(this.seniorId);
    }
}

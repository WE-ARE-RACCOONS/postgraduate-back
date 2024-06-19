package com.postgraduate.domain.senior.domain.entity;

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
@Table(indexes = {
        @Index(name = "senior_total_info_index", columnList = "totalInfo"),
        @Index(name = "senior_hit_index", columnList = "hit"),
        @Index(name = "senior_field_index", columnList = "field"),
        @Index(name = "senior_etc_field_index", columnList = "etcField"),
        @Index(name = "senior_postgradu_index", columnList = "postgradu"),
        @Index(name = "senior_etc_postgradu_index", columnList = "etcPostgradu")
})
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

    @Column(columnDefinition = "TEXT")
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

    public void updateInfo(Info info) {
        this.info = info;
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
}

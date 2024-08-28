package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
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
import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;

@Entity
@Table(indexes = {
        @Index(name = "senior_total_info_index", columnList = "totalInfo"),
        @Index(name = "senior_hit_index", columnList = "hit"),
        @Index(name = "senior_field_index", columnList = "field"),
        @Index(name = "senior_etc_field_index", columnList = "etcField"),
        @Index(name = "senior_postgradu_index", columnList = "postgradu"),
        @Index(name = "senior_etc_postgradu_index", columnList = "etcPostgradu"),
        @Index(name = "senior_mentoring_hit_index", columnList = "mentoringHit")
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

    @Column(nullable = false)
    private int mentoringHit;

    @Embedded
    private Info info;

    @Embedded
    private Profile profile;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "senior")
    private List<Available> availables = new ArrayList<>();

    @OneToOne(mappedBy = "senior")
    private Account account;

    public void updateAccount(SeniorMyPageUserAccountRequest myPageUserAccountRequest, String accountNumber) {
        account.updateMyPageUserAccount(myPageUserAccountRequest, accountNumber);
    }

    public void addAvailable(List<Available> availables) {
        this.availables.clear();
        this.availables.addAll(availables);
    }

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

    public void plusMentoringHit() {
        this.mentoringHit++;
    }

    public void minusMentoringHit() {
        this.mentoringHit--;
    }
}

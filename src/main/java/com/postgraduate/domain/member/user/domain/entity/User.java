package com.postgraduate.domain.member.user.domain.entity;

import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;

@Entity
@Table(indexes = {
        @Index(name = "user_nick_name_index", columnList = "nickName"),
        @Index(name = "user_is_delete", columnList = "isDelete")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private Long socialId;

    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String profile;

    @Column(nullable = false)
    private int point;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Builder.Default
    private List<MemberRole> roles = new ArrayList<>();

    @Column(nullable = false)
    private Boolean marketingReceive;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean isTutorial = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDelete = false;

    public boolean isJunior() {
        return roles.stream()
                .map(MemberRole::getRole)
                .toList()
                .contains(Role.USER);
    }

    public boolean isSenior() {
        return roles.stream()
                .map(MemberRole::getRole)
                .toList()
                .contains(Role.SENIOR);
    }

    public boolean isAdmin() {
        return roles.stream()
                .map(MemberRole::getRole)
                .toList()
                .contains(Role.ADMIN);
    }

    public void updateRole(MemberRole memberRole) {
        roles.add(memberRole);
    }

    public void updateInfo(String profile, String nickName, String phoneNumber) {
        this.profile = profile;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updateDelete() {
        this.isDelete = true;
    }

    public void restoreDelete() {
        this.isDelete = false;
    }

    public boolean isDelete() {
        return this.isDelete;
    }

    public boolean isTutorial() {
        return this.isTutorial;
    }

    public void tutorialFin() {
        this.isTutorial = true;
    }

    public boolean isDefaultProfile(List<String> defaultProfile) {
        return defaultProfile.contains(profile);
    }

    public boolean isRealDelete() {
        return (
                this.isDelete
                        &&
                        this.updatedAt.isBefore(
                                now().minusDays(15)
                                        .atStartOfDay()
                        )
        );
    }

    public boolean isHardDelete() {
        return (
                this.isDelete
                        &&
                        this.updatedAt.isBefore(
                                now().minusDays(30)
                                        .atStartOfDay()
                        )
        );
    }
}

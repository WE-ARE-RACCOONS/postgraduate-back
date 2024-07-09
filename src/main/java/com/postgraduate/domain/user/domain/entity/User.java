package com.postgraduate.domain.user.domain.entity;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(nullable = false)
    private Boolean marketingReceive;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDelete = false;

    public void updateRole(Role role) {
        this.role = role;
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

    public boolean isDelete() {
        return this.isDelete;
    }

    public boolean isDefaultProfile(List<String> defaultProfile) {
        return defaultProfile.contains(profile);
    }
}

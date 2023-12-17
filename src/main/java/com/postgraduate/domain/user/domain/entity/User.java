package com.postgraduate.domain.user.domain.entity;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDate;

@Entity
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
    @Builder.Default //이후에 기본 이미지 생기면 수정이 필요할 듯
    private String profile = "default";

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
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDelete = false;

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateInfo(String profile, String nickName, String phoneNumber) {
        this.profile = profile;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public void updateDelete() {
        this.isDelete = true;
    }
}

package com.postgraduate.domain.user.domain.entity;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity(name = "user")
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

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickName;

    @Column(nullable = false)
    @ColumnDefault("default") //이후에 기본 이미지 생기면 수정이 필요할 듯
    private String profile;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
}

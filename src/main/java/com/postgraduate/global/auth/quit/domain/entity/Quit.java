package com.postgraduate.global.auth.quit.domain.entity;

import com.postgraduate.domain.member.user.domain.entity.constant.Role;
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
public class Quit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quitId;

    private String reason;

    private String etc;

    @Enumerated(EnumType.STRING)
    private Role role;
}

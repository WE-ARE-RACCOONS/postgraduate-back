package com.postgraduate.domain.user.quit.domain.entity;

import com.postgraduate.domain.user.quit.domain.entity.constant.QuitReason;
import com.postgraduate.domain.user.user.domain.entity.constant.Role;
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

    @Enumerated(EnumType.STRING)
    private QuitReason reason;

    private String etc;

    @Enumerated(EnumType.STRING)
    private Role role;
}

package com.postgraduate.domain.wish.domain.entity;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.constant.Status;
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
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    private String major;

    private String field;

    @Column(nullable = false)
    private Boolean matchingReceive;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public void updateStatus(Status status) {
        this.status = status;
    }
}

package com.postgraduate.domain.wish.domain.entity;

import com.postgraduate.domain.user.domain.entity.User;
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

    @OneToOne
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status = false;

    public void updateStatus() {
        this.status = true;
    }
}

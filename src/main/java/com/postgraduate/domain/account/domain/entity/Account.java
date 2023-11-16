package com.postgraduate.domain.account.domain.entity;

import com.postgraduate.domain.senior.domain.entity.Senior;
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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String accountHolder;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String rrn;

    @OneToOne
    private Senior senior;
}

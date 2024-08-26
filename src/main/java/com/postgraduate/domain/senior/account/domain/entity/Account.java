package com.postgraduate.domain.senior.account.domain.entity;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
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

    @OneToOne(fetch = FetchType.LAZY)
    private Senior senior;

    public void updateMyPageUserAccount(SeniorMyPageUserAccountRequest myPageUserAccountRequest, String accountNumber) {
        this.accountNumber = accountNumber;
        this.bank = myPageUserAccountRequest.bank();
        this.accountHolder = myPageUserAccountRequest.accountHolder();
    }
}

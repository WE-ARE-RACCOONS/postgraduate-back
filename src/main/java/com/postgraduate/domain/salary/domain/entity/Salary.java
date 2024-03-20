package com.postgraduate.domain.salary.domain.entity;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.senior.domain.entity.Senior;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @Column(nullable = false)
    @Builder.Default
    private boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Senior senior;

    @Builder.Default
    @Column(nullable = false)
    private int totalAmount = 0;

    @Column(nullable = false)
    private LocalDate salaryDate;

    @Column
    private LocalDateTime salaryDoneDate;

    private String bank;
    private String accountNumber;
    private String accountHolder;

    public boolean status() {
        return this.status;
    }

    public void updateStatus(Boolean status) {
        this.status = status;
        this.salaryDoneDate = LocalDateTime.now();
    }

    public void updateAccount(String bank, String accountNumber, String accountHolder) {
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
    }

    public void plusAmount(int amount) {
        this.totalAmount += amount;
    }

    public void minusAmount(int amount) {
        this.totalAmount -= amount;
    }

    public SalaryStatus getSalaryStatus() {
        if (this.totalAmount == 0)
            return NONE;
        if (this.status())
            return DONE;
        return YET;
    }
}

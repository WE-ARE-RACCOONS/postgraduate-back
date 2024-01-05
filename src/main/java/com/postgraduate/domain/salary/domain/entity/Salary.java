package com.postgraduate.domain.salary.domain.entity;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private Boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Senior senior;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL)
    private List<Payment> payments;

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

    public void updateStatus(Boolean status) {
        this.status = status;
        this.salaryDoneDate = LocalDateTime.now();
    }

    public void plusAmount(int amount) {
        this.totalAmount += amount;
    }
}

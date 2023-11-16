package com.postgraduate.domain.salary.domain.entity;

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
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @Column(nullable = false)
    private String month;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status = false;

    @ManyToOne
    private Senior senior;

    public void updateAmount(int amount) {
        this.amount += (amount-4000);
    }
}

package com.postgraduate.domain.salary.domain.entity;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.senior.domain.entity.Senior;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Mentoring mentoring;

    @Column(nullable = false)
    private LocalDate salaryDate;

    @Column
    private LocalDateTime salaryDoneDate;

    public void updateStatus(Boolean status) {
        this.status = status;
        this.salaryDoneDate = LocalDateTime.now();
    }
}

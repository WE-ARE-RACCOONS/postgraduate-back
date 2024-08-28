package com.postgraduate.domain.mentoring.domain.entity;

import com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.WAITING;
import static com.postgraduate.domain.mentoring.domain.entity.constant.TermUnit.SHORT;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
public class Mentoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mentoringId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Senior senior;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Salary salary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    @Builder.Default
    private int term = 30;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MentoringStatus status = WAITING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "mentoring")
    private Refuse refuse;

    public void updateStatus(MentoringStatus mentoringStatus) {
        this.status = mentoringStatus;
    }

    public void updateSalary(Salary salary) {
        this.salary = salary;
    }

    public void updateDate(String date) {
        this.date = date;
    }

    public void updateUserDelete() {
        this.user = null;
    }

    public void updateSeniorDelete() {
        this.senior = null;
    }

    public boolean checkAutoDone() {
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime doneDate = parse(this.date, formatter);
        return now().minusDays(3)
                .isAfter(doneDate);
    }

    public int calculateForSenior() {
        int pay = this.payment.getPay();
        return pay - SHORT.getCharge();
    }
}

package com.postgraduate.domain.mentoring.domain.entity;

import com.postgraduate.domain.auth.exception.PermissionDeniedException;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.exception.MentoringDetailNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringNotExpectedException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
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

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
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
    private Status status = WAITING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateSalary(Salary salary) {
        this.salary = salary;
    }

    public void updateDate(String date) {
        this.date = date;
    }

    public boolean checkIsWaiting() {
        if (this.status == WAITING)
            return true;
        throw new MentoringNotWaitingException();
    }

    public boolean checkIsExpected() {
        if (this.status == EXPECTED)
            return true;
        throw new MentoringNotExpectedException();
    }

    public boolean checkDetailCondition() {
        if (this.status == WAITING || this.status == EXPECTED)
            return true;
        throw new MentoringDetailNotFoundException();
    }

    public boolean checkIsMineWithUser(User user) {
        if (this.user.equals(user))
            return true;
        log.error("userId = {}", user.getUserId());
        log.error("mentoring.getUserId = {}", this.user.getUserId());
        throw new PermissionDeniedException();
    }

    public boolean checkIsMineWithSenior(Senior senior) {
        if (this.senior.equals(senior))
            return true;
        log.error("senior = {}", senior.getSeniorId());
        log.error("mentoring.getSeniorId = {}", this.senior.getSeniorId());
        throw new PermissionDeniedException();
    }

    public boolean checkAutoDone() {
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime doneDate = parse(this.date, formatter);
        return now().minusDays(3)
                .isAfter(doneDate);
    }
}

package com.postgraduate.domain.mentoring.domain.entity;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Mentoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mentoringId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Senior senior;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int pay;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private LocalDate createdAt;

    private LocalDate deletedAt;
}

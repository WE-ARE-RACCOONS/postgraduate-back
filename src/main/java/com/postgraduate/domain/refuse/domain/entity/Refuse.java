package com.postgraduate.domain.refuse.domain.entity;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
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
public class Refuse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refuseId;

    @Column(nullable = false)
    private String reason;

    @OneToOne(fetch = FetchType.LAZY)
    private Mentoring mentoring;
}

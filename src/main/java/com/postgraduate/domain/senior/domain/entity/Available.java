package com.postgraduate.domain.senior.domain.entity;

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
public class Available {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availableId;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Senior senior;
}

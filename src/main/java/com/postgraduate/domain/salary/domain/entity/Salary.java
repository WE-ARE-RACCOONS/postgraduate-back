package com.postgraduate.domain.salary.domain.entity;

import com.postgraduate.domain.senior.domain.entity.Senior;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    private LocalDate month; //todo : 타입 고민

    private int amount;

    private Boolean status;

    @ManyToOne
    private Senior senior;
}

package com.postgraduate.domain.review.domain.entity;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.review.domain.entity.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @OneToOne
    private Mentoring mentoring;
    @Lob
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    @ColumnDefault("'REJECT'")
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate updatedAt;
    // 삭제가 추가된다면 삭제인지 아닌지 상태가 있어야하는데, 없어서 일단 뺌
}

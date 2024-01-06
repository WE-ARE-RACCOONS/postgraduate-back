package com.postgraduate.domain.payment.domain.entity;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    private Mentoring mentoring;

    @ManyToOne(fetch = FetchType.LAZY)
    private Salary salary;

    @Column(nullable = false)
    private int pay;

    private String cardAuthNumber;

    private String cardReceipt;

    private LocalDateTime paidAt; //결제사에서 받은 결제 시점

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = DONE;

    public void updateStatus(Status status) {
        this.status = status;
        this.deletedAt = LocalDateTime.now();
    }
}

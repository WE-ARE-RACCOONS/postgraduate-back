package com.postgraduate.domain.payment.domain.entity;

import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Senior senior;

    @Column(nullable = false)
    private int pay;

    @Column(unique = true)
    private String orderId;

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

    public void updateUserDelete() {
        this.user = null;
    }
}

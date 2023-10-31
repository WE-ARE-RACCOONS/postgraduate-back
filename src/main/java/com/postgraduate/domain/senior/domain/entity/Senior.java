package com.postgraduate.domain.senior.domain.entity;

import com.postgraduate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Senior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seniorId;
    @OneToOne
    private User user;
    private String name;
    @Column(nullable = false)
    private String college;
    @Column(nullable = false)
    private String major;
    @Column(nullable = false)
    private String postgradu;
    @Column(nullable = false)
    private String lab;
    @Column(nullable = false)
    private String field;
    private String info;
    private String target;
    private String chatLink;
    private String time;
    private int term;
    private String certification;
    @Column(nullable = false)
    private boolean status;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int hit;
}

package com.example.demo.domain;

import com.example.demo.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Keyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name; // ex : 퇴직 후의 삶이 걱정돼요.

    @Column(nullable = false, length= 100 )
    private String category; // ex : 건강 문제

    @Column(nullable = false, length=50)
    private String emoji;

    @Column(nullable=false, length =50)
    private String ageGroup;

}

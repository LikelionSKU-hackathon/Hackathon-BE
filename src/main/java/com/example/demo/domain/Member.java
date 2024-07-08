package com.example.demo.domain;

import com.example.demo.domain.common.BaseEntity;
import com.example.demo.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =40)
    private String name;

    @Column(nullable = false, length =40)
    private String userId;

    @Column(nullable = false, length =40)
    private String email;

    @Column(nullable = false, length =40)
    private Integer age;

    @Column(nullable = false, length =40)
    private String password;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;


}

package com.example.demo.domain;

import com.example.demo.domain.common.BaseEntity;
import com.example.demo.domain.mapping.MemberQuestion;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AIQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length =100)
    private String category;

    @OneToMany(mappedBy ="aiQuestion", cascade= CascadeType.ALL)
    private List<Diary> diaryList = new ArrayList<>();

    @OneToMany(mappedBy = "aiQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberQuestion> memberQuestions = new ArrayList<>();

}

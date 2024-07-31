package com.example.demo.domain;

import com.example.demo.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Mood extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String moodImage;

    @OneToMany(mappedBy ="mood", cascade= CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Diary> diaryList = new ArrayList<>();
}

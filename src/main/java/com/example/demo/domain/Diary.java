package com.example.demo.domain;

import com.example.demo.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 60)
    private String title;

    private boolean isPublic;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mood_id")
    private Mood mood;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="question_id")
    private AIQuestion aiQuestion;

    @OneToMany(mappedBy ="diary", cascade= CascadeType.ALL)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy ="diary", cascade= CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy ="diary", cascade= CascadeType.ALL)
    private List<AIComment> AICommentList = new ArrayList<>();

    private int likeCount; // 좋아요 갯수 필드 추가





}

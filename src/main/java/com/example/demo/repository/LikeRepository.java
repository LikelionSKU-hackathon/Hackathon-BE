package com.example.demo.repository;

import com.example.demo.domain.Diary;
import com.example.demo.domain.Likes;
import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByDiaryAndMember(Diary diary, Member member);
}

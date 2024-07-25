package com.example.demo.repository;

import com.example.demo.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByIsPublicTrue();
    List<Diary> findByMemberId(Long memberId);
    Optional<Diary> findTopByOrderByLikeCountDesc();

}

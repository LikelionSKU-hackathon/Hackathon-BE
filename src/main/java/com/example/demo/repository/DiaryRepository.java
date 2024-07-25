package com.example.demo.repository;

import com.example.demo.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByIsPublicTrue();

    @Query("SELECT d FROM Diary d WHERE d.member.id = :memberId AND FUNCTION('MONTH', d.createdAt) = :month AND FUNCTION('YEAR', d.createdAt) = :year")
    List<Diary> findByMemberIdAndMonth(@Param("memberId") Long memberId, @Param("month") int month, @Param("year") int year);
}

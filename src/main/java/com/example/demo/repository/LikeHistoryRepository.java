package com.example.demo.repository;

import com.example.demo.domain.LikeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Long> {
    List<LikeHistory> findByDate(LocalDate date);
}
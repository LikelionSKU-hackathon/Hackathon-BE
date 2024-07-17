package com.example.demo.repository;

import com.example.demo.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByAgeGroup(String ageGroup);
}

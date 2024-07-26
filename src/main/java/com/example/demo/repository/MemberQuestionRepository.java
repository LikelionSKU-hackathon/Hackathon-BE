package com.example.demo.repository;

import com.example.demo.domain.mapping.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Long> {
}

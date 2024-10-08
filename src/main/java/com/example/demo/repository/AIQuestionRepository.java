package com.example.demo.repository;

import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.mapping.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AIQuestionRepository extends JpaRepository<AIQuestion, Long> {

}

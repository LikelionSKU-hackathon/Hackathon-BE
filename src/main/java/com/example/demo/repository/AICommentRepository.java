package com.example.demo.repository;

import com.example.demo.domain.AIComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AICommentRepository extends JpaRepository<AIComment, Long> {
}

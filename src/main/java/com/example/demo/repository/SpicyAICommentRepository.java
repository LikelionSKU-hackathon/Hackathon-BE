package com.example.demo.repository;

import com.example.demo.domain.SpicyAIComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpicyAICommentRepository extends JpaRepository<SpicyAIComment, Long>
{
}

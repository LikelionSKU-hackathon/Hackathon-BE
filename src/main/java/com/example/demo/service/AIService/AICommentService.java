package com.example.demo.service.AIService;

import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.Diary;

import java.util.Optional;

public interface AICommentService {
    AIQuestion generateAIQuestion(Long memberId);
    Diary generateAIComment(Long diaryId);
    Diary generateSpicyAIComment(Long diaryId);
    Diary getDiaryWithSpicyComment(Long diaryId);
}

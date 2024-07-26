package com.example.demo.service.DiaryCommandService;

import com.example.demo.domain.Diary;
import com.example.demo.web.dto.DiaryResponseDTO;

import java.util.List;

public interface DiaryQueryService {
    List<Diary> getDiaryList();

    DiaryResponseDTO.EmojiResultDTO getDiariesByMonth(int year, int month, Long memberId);
}


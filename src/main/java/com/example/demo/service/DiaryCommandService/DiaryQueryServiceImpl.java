package com.example.demo.service.DiaryCommandService;

import com.example.demo.converter.DiaryConverter;
import com.example.demo.domain.Diary;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.web.dto.DiaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryQueryServiceImpl implements DiaryQueryService{
    private final DiaryRepository diaryRepository;
    private final DiaryConverter diaryConverter;
    @Override
    public List<Diary> getDiaryList (){
        return diaryRepository.findByIsPublicTrue();
    }

    @Transactional(readOnly = true)
    public DiaryResponseDTO.EmojiResultDTO getDiariesByMonth(int year, int month, Long memberId) {
        List<Diary> diaries = diaryRepository.findByMemberIdAndMonth(memberId, month, year);
        List<DiaryResponseDTO.EmojiDTO> emojiDTOList = diaries.stream()
                .map(diaryConverter::convertToEmojiDto)
                .collect(Collectors.toList());
        return DiaryResponseDTO.EmojiResultDTO.builder().emojiDTOList(emojiDTOList).build();
    }
}

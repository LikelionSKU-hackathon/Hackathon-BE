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
        return diaryRepository.findByIsPublicTrueOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public DiaryResponseDTO.EmojiResultDTO getDiariesByMonth(int year, int month, Long memberId) {
        List<Diary> diaries = diaryRepository.findByMemberIdAndMonth(memberId, month, year);// memberId와 month를 기준으로 diaryList를 반환받음
        List<DiaryResponseDTO.EmojiDTO> emojiDTOList = diaries.stream() //diary 객체를 diaryConverter를 통해 emojiDTO로 만들고 그걸 list로 만들어 반환
                .map(diaryConverter::convertToEmojiDto)
                .collect(Collectors.toList());
        return DiaryResponseDTO.EmojiResultDTO.builder().emojiDTOList(emojiDTOList).build();
    }
}

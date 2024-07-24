package com.example.demo.service.DiaryService;

import com.example.demo.domain.Diary;
import com.example.demo.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryQueryServiceImpl implements DiaryQueryService{
    private final DiaryRepository diaryRepository;
    @Override
    public List<Diary> getDiaryList (){
        return diaryRepository.findByIsPublicTrue();
    }
}

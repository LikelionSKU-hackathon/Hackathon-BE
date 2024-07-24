package com.example.demo.service;

import com.example.demo.domain.Diary;
import com.example.demo.domain.LikeHistory;
import com.example.demo.domain.Member;
import com.example.demo.web.dto.DiaryRequestDTO;
import com.example.demo.web.dto.DiaryResponseDTO;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.LikeHistoryRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final LikeHistoryRepository likeHistoryRepository;

    public DiaryResponseDTO createDiary(DiaryRequestDTO diaryRequestDTO) {
        Optional<Member> memberOptional = memberRepository.findById(diaryRequestDTO.getMemberId());
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Member member = memberOptional.get();
        Diary diary = Diary.builder()
                .title(diaryRequestDTO.getTitle())
                .content(diaryRequestDTO.getContent())
                .isPublic(diaryRequestDTO.isPublic())
                .member(member)
                .likeCount(0) // 좋아요 갯수 초기화
                .build();

        Diary savedDiary = diaryRepository.save(diary);

        return DiaryResponseDTO.builder()
                .id(savedDiary.getId())
                .title(savedDiary.getTitle())
                .content(savedDiary.getContent())
                .isPublic(savedDiary.isPublic())
                .memberUsername(member.getUsername())
                .likeCount(savedDiary.getLikeCount()) // 좋아요 갯수 포함
                .build();
    }

    public DiaryResponseDTO getDiary(Long diaryId) {
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        if (!diaryOptional.isPresent()) {
            throw new RuntimeException("Diary not found");
        }

        Diary diary = diaryOptional.get();
        return DiaryResponseDTO.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .isPublic(diary.isPublic())
                .memberUsername(diary.getMember().getUsername())
                .likeCount(diary.getLikeCount()) // 좋아요 갯수 포함
                .build();
    }

    public List<DiaryResponseDTO> getDiariesByMember(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        List<Diary> diaries = diaryRepository.findByMemberId(memberId);
        return diaries.stream()
                .map(diary -> DiaryResponseDTO.builder()
                        .id(diary.getId())
                        .title(diary.getTitle())
                        .content(diary.getContent())
                        .isPublic(diary.isPublic())
                        .memberUsername(diary.getMember().getUsername())
                        .likeCount(diary.getLikeCount()) // 좋아요 갯수 포함
                        .build())
                .collect(Collectors.toList());
    }

    public DiaryResponseDTO getMostLikedDiary() {
        Optional<Diary> diaryOptional = diaryRepository.findTopByOrderByLikeCountDesc();
        if (!diaryOptional.isPresent()) {
            throw new RuntimeException("No diaries found");
        }

        Diary diary = diaryOptional.get();
        return DiaryResponseDTO.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .isPublic(diary.isPublic())
                .memberUsername(diary.getMember().getUsername())
                .likeCount(diary.getLikeCount()) // 좋아요 갯수 포함
                .build();
    }

    public DiaryResponseDTO getMostLikedDiaryToday() {
        LocalDate today = LocalDate.now();
        List<LikeHistory> todayLikeHistories = likeHistoryRepository.findByDate(today);

        if (todayLikeHistories.isEmpty()) {
            throw new RuntimeException("No diaries found for today");
        }

        LikeHistory mostLiked = todayLikeHistories.stream()
                .max((h1, h2) -> Integer.compare(h1.getLikeCount(), h2.getLikeCount()))
                .orElseThrow(() -> new RuntimeException("No diaries found for today"));

        Diary diary = mostLiked.getDiary();
        return DiaryResponseDTO.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .isPublic(diary.isPublic())
                .memberUsername(diary.getMember().getUsername())
                .likeCount(diary.getLikeCount()) // 좋아요 갯수 포함
                .build();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetLikeCounts() {
        List<Diary> diaries = diaryRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Diary diary : diaries) {
            LikeHistory likeHistory = LikeHistory.builder()
                    .diary(diary)
                    .likeCount(diary.getLikeCount())
                    .date(today)
                    .build();

            likeHistoryRepository.save(likeHistory);

            diary.setLikeCount(0);
            diaryRepository.save(diary);
        }
    }
}

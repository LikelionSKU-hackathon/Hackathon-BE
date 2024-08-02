package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.repository.*;
import com.example.demo.web.dto.DiaryRequestDTO;
import com.example.demo.web.dto.DiaryResponseDTO;
import com.example.demo.service.AIService.AICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final MoodRepository moodRepository;
    private final AICommentService aiCommentService;
    private final AIQuestionRepository aiQuestionRepository;
    private final LikeRepository likeRepository;

    public DiaryResponseDTO createDiary(DiaryRequestDTO diaryRequestDTO) {
        Optional<Member> memberOptional = memberRepository.findById(diaryRequestDTO.getMemberId());
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Optional<Mood> moodOptional = moodRepository.findById(diaryRequestDTO.getMoodId());
        if (!moodOptional.isPresent()) {
            throw new RuntimeException("Mood not found");
        }

        Member member = memberOptional.get();
        Mood mood = moodOptional.get();

        String title = diaryRequestDTO.getTitle();
        if (diaryRequestDTO.getQuestionId() != null) {
            Optional<AIQuestion> aiQuestionOptional = aiQuestionRepository.findById(diaryRequestDTO.getQuestionId());
            if (aiQuestionOptional.isPresent()) {
                AIQuestion aiQuestion = aiQuestionOptional.get();
                title = aiQuestion.getContent();
            }
        }
        Diary diary = Diary.builder()
                .title(title)
                .content(diaryRequestDTO.getContent())
                .isPublic(diaryRequestDTO.isPublic())
                .member(member)
                .category(diaryRequestDTO.getCategory())
                .mood(mood)
                .likeCount(0)
                .build();

        Diary savedDiary = diaryRepository.save(diary);

        //aiCommentService.generateAIComment(savedDiary.getId());

        return DiaryResponseDTO.builder()
                .id(savedDiary.getId())
                .title(savedDiary.getTitle())
                .content(savedDiary.getContent())
                .isPublic(savedDiary.isPublic())
                .category(savedDiary.getCategory())
                .memberUsername(member.getUsername())
                .likeCount(savedDiary.getLikeCount())
                .moodName(mood.getName())
                .moodImage(mood.getMoodImage())
                .createdAt(savedDiary.getCreatedAt())
                .build();
    }

    public DiaryResponseDTO createDiaryWithAIQuestion(DiaryRequestDTO diaryRequestDTO) {
        Optional<Member> memberOptional = memberRepository.findById(diaryRequestDTO.getMemberId());
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Optional<Mood> moodOptional = moodRepository.findById(diaryRequestDTO.getMoodId());
        if (!moodOptional.isPresent()) {
            throw new RuntimeException("Mood not found");
        }

        Optional<AIQuestion> aiQuestionOptional = aiQuestionRepository.findById(diaryRequestDTO.getQuestionId());
        if (!aiQuestionOptional.isPresent()) {
            throw new RuntimeException("AI Question not found");
        }

        Member member = memberOptional.get();
        Mood mood = moodOptional.get();
        AIQuestion aiQuestion = aiQuestionOptional.get();

        Diary diary = Diary.builder()
                .title(aiQuestion.getContent())
                .content(diaryRequestDTO.getContent())
                .isPublic(diaryRequestDTO.isPublic())
                .category(diaryRequestDTO.getCategory())
                .member(member)
                .mood(mood)
                .aiQuestion(aiQuestion)
                .likeCount(0)
                .build();

        Diary savedDiary = diaryRepository.save(diary);
        //aiCommentService.generateAIComment(savedDiary.getId());

        return DiaryResponseDTO.builder()
                .id(savedDiary.getId())
                .title(savedDiary.getTitle())
                .content(savedDiary.getContent())
                .isPublic(savedDiary.isPublic())
                .category(savedDiary.getCategory())
                .memberUsername(member.getUsername())
                .likeCount(savedDiary.getLikeCount())
                .moodName(mood.getName())
                .moodImage(mood.getMoodImage())
                .createdAt(savedDiary.getCreatedAt())
                //.aiComments(savedDiary.getAiComment() != null ? List.of(savedDiary.getAiComment().getContent()) : null)
                .build();
    }

    public DiaryResponseDTO getDiary(Long diaryId) {
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        if (!diaryOptional.isPresent()) {
            throw new RuntimeException("Diary not found");
        }

        Diary diary = diaryOptional.get();
        Mood mood = diary.getMood();
        AIComment aiComment = diary.getAiComment();

        return DiaryResponseDTO.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .isPublic(diary.isPublic())
                .memberUsername(diary.getMember().getUsername())
                .likeCount(diary.getLikeCount())
                .moodName(mood != null ? mood.getName() : null)
                .moodImage(mood != null ? mood.getMoodImage() : null)
                .createdAt(diary.getCreatedAt())
                .aiComments(aiComment != null ? List.of(aiComment.getContent()) : null)  // AI 댓글 포함
                .build();
    }
    public List<DiaryResponseDTO> getDiariesByMember(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        List<Diary> diaries = diaryRepository.findByMemberId(memberId);
        return diaries.stream()
                .map(diary -> {
                    Mood mood = diary.getMood();
                    AIComment aiComment = diary.getAiComment();

                    return DiaryResponseDTO.builder()
                            .id(diary.getId())
                            .title(diary.getTitle())
                            .content(diary.getContent())
                            .isPublic(diary.isPublic())
                            .memberUsername(diary.getMember().getUsername())
                            .likeCount(diary.getLikeCount())
                            .moodName(mood != null ? mood.getName() : null)
                            .moodImage(mood != null ? mood.getMoodImage() : null)
                            .createdAt(diary.getCreatedAt())
                            .aiComments(aiComment != null ? List.of(aiComment.getContent()) : null)  // AI 댓글 포함
                            .build();
                })
                .collect(Collectors.toList());
    }

    public DiaryResponseDTO.DiaryPreInfoDTO getDiaryPreInfo(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }
        Member member = memberOptional.get();
        int diaryCount = diaryRepository.countByMember(member);

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
        String username = member.getUsername();

        return DiaryResponseDTO.DiaryPreInfoDTO.builder()
                .date(date)
                .username(username)
                .diaryCount(diaryCount + 1) // 작성할 글의 순서이므로 +1
                .build();
    }
    public List<DiaryResponseDTO> getMostLikedDiaries() {
        List<Diary> diaries = diaryRepository.findTop2ByOrderByLikeCountDesc();
        if (diaries.isEmpty()) {
            throw new RuntimeException("No diaries found");
        }

        return diaries.stream()
                .map(diary -> {
                    Mood mood = diary.getMood();
                    AIComment aiComment = diary.getAiComment();

                    return DiaryResponseDTO.builder()
                            .id(diary.getId())
                            .title(diary.getTitle())
                            .content(diary.getContent())
                            .isPublic(diary.isPublic())
                            .memberUsername(diary.getMember().getUsername())
                            .likeCount(diary.getLikeCount())
                            .moodName(mood != null ? mood.getName() : null)
                            .moodImage(mood != null ? mood.getMoodImage() : null)
                            .createdAt(diary.getCreatedAt())
                            .aiComments(aiComment != null ? List.of(aiComment.getContent()) : null)  // AI 댓글 포함
                            .build();
                })
                .collect(Collectors.toList());
    }
    public boolean getILiked(Long diaryId, Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        if (!diaryOptional.isPresent()) {
            throw new RuntimeException("Diary not found");
        }

        Diary diary = diaryOptional.get();
        boolean iLiked = likeRepository.findByDiaryAndMember(diary, memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"))).isPresent();
        diary.setILiked(iLiked);

        return iLiked;
    }
}

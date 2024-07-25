package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Diary;
import com.example.demo.domain.Member;
import com.example.demo.web.dto.CommentRequestDTO;
import com.example.demo.web.dto.CommentResponseDTO;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        Optional<Diary> diaryOptional = diaryRepository.findById(commentRequestDTO.getDiaryId());
        if (!diaryOptional.isPresent()) {
            throw new RuntimeException("Diary not found");
        }

        Optional<Member> memberOptional = memberRepository.findById(commentRequestDTO.getMemberId());
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Diary diary = diaryOptional.get();
        Member member = memberOptional.get();

        Comment comment = Comment.builder()
                .content(commentRequestDTO.getContent())
                .diary(diary)
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .memberUsername(member.getUsername())
                .diaryTitle(diary.getTitle())  // 일기 제목 추가
                .diaryContent(diary.getContent()) // 일기 내용 추가
                .build();
    }

    public List<CommentResponseDTO> getCommentsByDiary(Long diaryId) {
        List<Comment> comments = commentRepository.findByDiaryId(diaryId);
        return comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .memberUsername(comment.getMember().getUsername())
                        .diaryTitle(comment.getDiary().getTitle())  // 일기 제목 추가
                        .diaryContent(comment.getDiary().getContent()) // 일기 내용 추가
                        .build())
                .collect(Collectors.toList());
    }
}

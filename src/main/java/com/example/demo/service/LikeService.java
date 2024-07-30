package com.example.demo.service;

import com.example.demo.domain.Diary;
import com.example.demo.domain.Likes;
import com.example.demo.domain.Member;
import com.example.demo.web.dto.LikeRequestDTO;
import com.example.demo.web.dto.LikeResponseDTO;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public LikeResponseDTO addLike(LikeRequestDTO likeRequestDTO) {
        Optional<Diary> diaryOptional = diaryRepository.findById(likeRequestDTO.getDiaryId());
        if (!diaryOptional.isPresent()) {
            throw new RuntimeException("Diary not found");
        }

        Optional<Member> memberOptional = memberRepository.findById(likeRequestDTO.getMemberId());
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Diary diary = diaryOptional.get();
        Member member = memberOptional.get();

        // 이미 좋아요를 추가했는지 확인
        Optional<Likes> existingLike = likeRepository.findByDiaryAndMember(diary, member);
        if (existingLike.isPresent()) {
            throw new RuntimeException("Member has already liked this diary");
        }

        Likes like = Likes.builder()
                .diary(diary)
                .member(member)
                .build();

        Likes savedLike = likeRepository.save(like);

        diary.setLikeCount(diary.getLikeCount() + 1);
        diaryRepository.save(diary);

        return LikeResponseDTO.builder()
                .id(savedLike.getId())
                .diaryId(diary.getId())
                .memberId(member.getId())
                .build();
    }

    public void removeLike(Long likeId) {
        Optional<Likes> likeOptional = likeRepository.findById(likeId);
        if (!likeOptional.isPresent()) {
            throw new RuntimeException("Like not found");
        }

        Likes like = likeOptional.get();
        Diary diary = like.getDiary();
        diary.setLikeCount(diary.getLikeCount() - 1);
        diaryRepository.save(diary);

        likeRepository.deleteById(likeId);
    }
}

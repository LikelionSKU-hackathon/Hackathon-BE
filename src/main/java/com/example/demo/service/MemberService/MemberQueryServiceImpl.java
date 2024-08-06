package com.example.demo.service.MemberService;

import com.example.demo.apiPayload.code.status.ErrorStatus;
import com.example.demo.apiPayload.exception.handler.MemberHandler;
import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;
import com.example.demo.repository.KeywordRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//읽기
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    //마이페이지 조회
    @Override
    public Member getMypage(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
    //키워드 조회
    @Override
    public List<Keyword> getKeyword(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<Keyword> keywords = keywordRepository.findByAgeGroup(member.getAgeGroup()); //  AgeGroup으로 데이터 조회
        if (keywords.isEmpty()) { // keyword 찾을 수 없으면 에러 발생
            throw new MemberHandler(ErrorStatus.KEYWORD_NOT_FOUND);
        }
        Collections.shuffle(keywords);
        return keywords;

    }
    //이메일 중복확인
    @Override
    public Boolean checkEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent(); // 존재하면 true , 존재하지 않으면 false
    }

    //닉네임 중복확인
    @Override
    public Boolean checkUsername(String username){
        Optional<Member> member = memberRepository.findByUsername(username);
        return member.isPresent(); // 존재하면 true , 존재하지 않으면 false
    }





}

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

import java.util.List;

@Service
@RequiredArgsConstructor
//읽기
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    @Override
    public Member getMypage(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
    @Override
    public List<Keyword> getKeyword(String age_group){
        List<Keyword> keywords = keywordRepository.findByAgeGroup(age_group);
        if (keywords.isEmpty()) {
            throw new MemberHandler(ErrorStatus.KEYWORD_NOT_FOUND);
        }
        return keywords;

    }


}

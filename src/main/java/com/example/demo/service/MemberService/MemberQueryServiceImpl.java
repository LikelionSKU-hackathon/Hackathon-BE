package com.example.demo.service.MemberService;

import com.example.demo.apiPayload.code.status.ErrorStatus;
import com.example.demo.apiPayload.exception.handler.MemberHandler;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//읽기
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;
    @Override
    public Member getMypage(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }


}

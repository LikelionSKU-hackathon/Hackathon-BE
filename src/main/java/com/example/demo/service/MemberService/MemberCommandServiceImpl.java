package com.example.demo.service.MemberService;

import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.web.dto.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//쓰기
@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;
    @Override
    public Member joinMember(MemberRequestDTO .JoinDTO request){
        Member newMember = MemberConverter.toMember(request);
        return memberRepository.save(newMember);
    }
}

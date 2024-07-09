package com.example.demo.service.MemberService;

import com.example.demo.auth.JwtToken;
import com.example.demo.auth.provider.JwtTokenProvider;
import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.web.dto.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

//쓰기
@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Override
    public Member joinMember(MemberRequestDTO .JoinDTO request){
        Member newMember = MemberConverter.toMember(request);
        return memberRepository.save(newMember);
    }

    @Override
    public JwtToken Login (MemberRequestDTO.LoginDTO request){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }
}
